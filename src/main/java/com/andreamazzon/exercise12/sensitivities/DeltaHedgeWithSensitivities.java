package com.andreamazzon.exercise12.sensitivities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.time.TimeDiscretization;


/**
 * This class performs delta hedging, with four possible methods for the computation of the delta:
 * analytic delta, sensitivity through central differences, sensitivity through Pathwise
 * Differentiation and sensitivity through Likelihood Ratio.
 * Note the difference between the Monte-Carlo model of the underlying, and the Monte-Carlo model
 * used to hedge. These are given in the constructor.
 *
 * @author Andrea Mazzon
 *
 */
public class DeltaHedgeWithSensitivities {


	final private double interestRate;//we suppose no uncertainty about it

	final private double volatilityHedge;//volatility of the model used to hedge
	final private double volatilityUnderlying;//volatility of the model of the underlying
	//it will be filled from monteCarloModelOfUnderlying

	//values for the option
	final private double optionStrike;
	final private double optionMaturity;

	final private TimeDiscretization timeDiscretization;

	final private MonteCarloBlackScholesModel monteCarloModelOfUnderlying;

	double[] pathOfUnderlying;//one trajectory: array of doubles
	double[] optionPrices;//given by the Monte-Carlo model of the underlying
	double[] deltas;//given by the Monte-Carlo model used to hedge

	double[] bankAccount;
	double[] hedgeError;
	double[] portfolioValue;

	int numberOfSimulationsForSensitivities;

	int pathLength;

	private final int seed = 1897;

	DeltaType type;

	public DeltaHedgeWithSensitivities(MonteCarloBlackScholesModel monteCarloModelOfUnderlying,
			double volatilityForHedge, double interestRate, double optionStrike, double optionMaturity, DeltaType type,
			int numberOfSimulationsForSensitivities) {
		this.volatilityHedge = volatilityForHedge;
		/*
		 * getVolatility is a method of BlackScholesModel, which is the "model" part of MonteCarloBlackScholesModel.
		 * So you first get the model, and then you can call the method
		 */
		volatilityUnderlying = monteCarloModelOfUnderlying.getModel().getVolatility().doubleValue();
		this.interestRate = interestRate;
		this.monteCarloModelOfUnderlying = monteCarloModelOfUnderlying;
		this.optionStrike = optionStrike;
		this.optionMaturity = optionMaturity;
		timeDiscretization = monteCarloModelOfUnderlying.getTimeDiscretization();
		pathLength = timeDiscretization.getNumberOfTimes();
		this.type = type;
		this.numberOfSimulationsForSensitivities = numberOfSimulationsForSensitivities;

	}

	/*
	 * This method generates one path of the underlying (given by monteCarloModelOfUnderlying).
	 */
	private void generatePricePath() throws CalculationException {
		pathOfUnderlying = new double[pathLength];
		for (int timeIndex = 0; timeIndex < pathLength; timeIndex++) {
			pathOfUnderlying[timeIndex] = monteCarloModelOfUnderlying.getAssetValue(timeIndex, 0)
					.get(0);//just one path! get(int indexOfSimulation) of RandomVariable

		}
	}

	/**
	 * This method generates the deltas and the prices of the option, with four different methods for
	 * the delta
	 * @param type: enum type, defines the method used to compute the deltas
	 * @throws CalculationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void generateDeltasAndPrices() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class<?> classForSensitivities;

		if (pathOfUnderlying == null) {
			generatePricePath();
		}

		optionPrices = new double[pathOfUnderlying.length];
		deltas = new double[pathOfUnderlying.length];

		switch (type) {
		case PATHWISE:
			classForSensitivities = EuropeanOptionDeltaLikelihood.class;
			break;
		case FINITE_DIFFERENCES:
			classForSensitivities = EuropeanOptionDeltaCentralDifferences.class;
			break;
		case LIKELIHOOD:
			classForSensitivities = EuropeanOptionDeltaLikelihood.class;
			break;
		default:
			classForSensitivities = null;
		}
		final Constructor<?> classConstructor = classForSensitivities.getConstructor(double.class, double.class);

		deltas[0] = 0;
		//first price
		optionPrices[0] = AnalyticFormulas.blackScholesOptionValue(pathOfUnderlying[0],
				interestRate, volatilityUnderlying, optionMaturity,optionStrike);
		for (int timeIndex = 1; timeIndex < pathOfUnderlying.length; timeIndex++) {

			final double evaluationTime = timeDiscretization.getTime(timeIndex);
			final double timeToMaturity = optionMaturity - evaluationTime;//new maturity
			final double newValueUnderlying = pathOfUnderlying[timeIndex];//model for the underlying!
			final AbstractAssetMonteCarloProduct deltaCalculator =
					(AbstractAssetMonteCarloProduct) classConstructor.newInstance(timeToMaturity, optionStrike);

			//construct an object of type MonteCarloAssetModel to pass to deltaCalculator.

			final MonteCarloAssetModel cloneMonteCarlo = null;

			deltas[timeIndex] = deltaCalculator.getValue(cloneMonteCarlo);
			//we price the option with the model of the underlying
			optionPrices[timeIndex] = AnalyticFormulas.blackScholesOptionValue(
					newValueUnderlying,interestRate, volatilityUnderlying, timeToMaturity,
					optionStrike);
		}
	}

	/*
	 * This method generates the value of the bank account, of the portfolio value and
	 * of the hedging errors.
	 */
	private void generateHedge() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		generateDeltasAndPrices();

		bankAccount = new double[pathLength];
		portfolioValue = new double[pathLength];
		hedgeError = new double[pathLength];

		portfolioValue[0] = optionPrices[0];
		bankAccount[0] = optionPrices[0];
		hedgeError[0] = 0; //at beginning, no error

		double amountOfUnderlying = 0;//initial amount of the underlying

		for (int timeIndex = 1; timeIndex < pathLength; timeIndex++) {

			final double numeraire = monteCarloModelOfUnderlying.getNumeraire(timeIndex).get(0);
			final double underlying = pathOfUnderlying[timeIndex];

			//value of current portfolio position
			portfolioValue[timeIndex] = amountOfUnderlying * underlying
					+ bankAccount[timeIndex - 1] * numeraire ;

			//error
			hedgeError[timeIndex] = optionPrices[timeIndex] - portfolioValue[timeIndex];

			//rebalance
			bankAccount[timeIndex] = bankAccount[timeIndex - 1]
					+ (deltas[timeIndex - 1] - deltas[timeIndex]) * underlying / numeraire;

			amountOfUnderlying = deltas[timeIndex];
		}
	}

	/**
	 * It returns the path of the underlying we have followed while hedging
	 * @return the path, as a vector of doubles
	 * @throws CalculationException
	 */
	public double[] getPathOfUnderlying() throws CalculationException {
		if (pathOfUnderlying == null) {
			generatePricePath();
		}
		return pathOfUnderlying;
	}

	/**
	 * It returns the path of the option prices
	 * @return the option prices, as a vector of doubles
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws Exception
	 */
	public double[] getOptionPrices() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (optionPrices == null) {
			generateDeltasAndPrices();
		}
		return optionPrices;
	}

	/**
	 * It returns the path of the option deltas
	 * @return the option deltas, as a vector of doubles
	 * @throws CalculationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public double[] getDeltas() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (deltas == null) {
			generateDeltasAndPrices();
		}
		return deltas;
	}

	/**
	 * It returns the path of the hedge error
	 * @return the hedge errors, as a vector of doubles
	 * @throws CalculationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public double[] getHedgeError() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (hedgeError == null) {
			generateHedge();
		}
		return hedgeError;
	}

	/**
	 * It returns the path of the portfolio value
	 * @return the portfolio values, as a vector of doubles
	 * @throws CalculationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public double[] getPortfolioValue() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (hedgeError == null) {
			generateHedge();
		}
		return portfolioValue;
	}

	/**
	 * It returns the path of the bank account
	 * @return the values of the bank account, as a vector of doubles
	 * @throws CalculationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public double[] getBankAccount() throws CalculationException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (hedgeError == null) {
			generateHedge();
		}
		return bankAccount;
	}


}
