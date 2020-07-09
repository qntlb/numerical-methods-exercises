package com.andreamazzon.exercise11.hedging;


import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.time.TimeDiscretization;


/**
 * This class performs delta hedging.
 * Note the difference between the volatility of the model of the underlying, and the one
 * used to hedge.
 *
 * @author Andrea Mazzon
 *
 */
public class DeltaHedge {


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

	int pathLength;

	public DeltaHedge(
			MonteCarloBlackScholesModel monteCarloModelOfUnderlying,
			double volatilityForHedge, double interestRate, double optionStrike, double optionMaturity) {
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

	/*
	 * This method generates the deltas and the prices of the option. Note that the deltas are
	 * generated basing on the volatility used for hedging (i.e., the "estimated" one) whereas the
	 * price of the options are given by the volatility of the underlying, i.e., the "true" volatility.
	 */
	private void generateDeltasAndPrices() throws CalculationException {
		generatePricePath();

		optionPrices = new double[pathLength];
		deltas = new double[pathLength];

		/*
		 * let's say is zero, because the first amount of underlying is zero. Not too clean maybe,
		 * but still maybe easier than constructing two arrays, i.e., deltas and quantityOfUnderlying.
		 */
		deltas[0] = 0;
		//first price
		optionPrices[0] = AnalyticFormulas.blackScholesOptionValue(pathOfUnderlying[0],
				interestRate, volatilityUnderlying, optionMaturity,optionStrike);

		for (int timeIndex = 1; timeIndex < pathLength; timeIndex++) {

			final double evaluationTime = timeDiscretization.getTime(timeIndex);
			final double timeToMaturity = optionMaturity - evaluationTime;//new time to maturity
			final double newValueUnderlying = pathOfUnderlying[timeIndex];//model for the underlying!

			//we compute the delta with the model used for hedging
			deltas[timeIndex] = AnalyticFormulas.blackScholesOptionDelta(
					newValueUnderlying, interestRate, volatilityHedge, timeToMaturity,
					optionStrike);

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
	private void generateHedge() throws CalculationException {

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
	 * @throws CalculationException
	 */
	public double[] getOptionPrices() throws CalculationException {
		if (optionPrices == null) {
			generateDeltasAndPrices();
		}
		return optionPrices;
	}

	/**
	 * It returns the path of the option deltas
	 * @return the option deltas, as a vector of doubles
	 * @throws CalculationException
	 */
	public double[] getDeltas() throws CalculationException {
		if (deltas == null) {
			generateDeltasAndPrices();
		}
		return deltas;
	}

	/**
	 * It returns the path of the hedge error
	 * @return the hedge errors, as a vector of doubles
	 * @throws CalculationException
	 */
	public double[] getHedgeError() throws CalculationException {
		if (hedgeError == null) {
			generateHedge();
		}
		return hedgeError;
	}

	/**
	 * It returns the path of the portfolio value
	 * @return the portfolio values, as a vector of doubles
	 * @throws CalculationException
	 */
	public double[] getPortfolioValue() throws CalculationException {
		if (hedgeError == null) {
			generateHedge();
		}
		return portfolioValue;
	}

	/**
	 * It returns the path of the bank account
	 * @return the values of the bank account, as a vector of doubles
	 * @throws CalculationException
	 */
	public double[] getBankAccount() throws CalculationException {
		if (hedgeError == null) {
			generateHedge();
		}
		return bankAccount;
	}

}
