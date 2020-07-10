package com.andreamazzon.exercise12.sensitivities;

import java.util.HashMap;
import java.util.Map;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;

/**
 * This class implements the computation of the delta of an European call option through central
 * differences, using Monte-Carlo in order to get the prices of the European call option. Note that
 * the only overridden method is getValue.
 *
 * @author Andrea Mazzon

 */
public class EuropeanOptionDeltaCentralDifferences extends AbstractAssetMonteCarloProduct {

	private final double maturity;
	private final double strike;
	private final double step = 0.05;

	public EuropeanOptionDeltaCentralDifferences(double maturity, double strike) {
		super();
		this.maturity = maturity;
		this.strike = strike;
	}

	/**
	 * This method returns the value of the delta of an European call option through central differences,
	 * as a RandomVariable.
	 *
	 * @param evaluationTime The time on which this products value should be observed.
	 * @param monteCarloModel, object of type AssetModelMonteCarloSimulationModel used to price the
	 * product.
	 * @return The random variable representing the value of the delta discounted to evaluation time
	 * @throws net.finmath.exception.CalculationException Thrown if the valuation fails, specific
	 * cause may be available via the <code>cause()</code> method.
	 */
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel
			underlyingSimulation) throws CalculationException {
		/*
		 * We want to compute the Monte-Carlo approximation at t of
		 * (V(S_t + delta) - V(S_t - delta))/(2 delta),
		 * where delta = 0.05 is the step of the differentiation and (S_r)_{r >= t} is represented by
		 * underlyingSimulation. V(S_t) is here the RandomVariable we get by calling the method
		 * getValue(maturity, underlyingSimulation) of EuropeanOption, where maturity is the time
		 * to maturity and underlyingSimulation has initial value S_t. Therefore, in order to get
		 * V(S_t + delta) and V(S_t - delta) we have to give to getValue a "clone" of underlyingSimulation
		 * with initial value S_t + delta, S_t - delta, respectively.
		 *
		 */

		//first step: get the initial value of underlyingSimulation. Look at the Finmath library.
		final RandomVariable initialValue = null;

		/*
		 * we want to construct a new model with modified initial data S_t + delta: to this purpose, we use
		 * the getCloneWithModifiedData method of MonteCarloAssetModel. The data is given by the code below.
		 */
		final Map<String, Object> dataForward = new HashMap<String, Object>();
		dataForward.put("initialValue", initialValue.add(step).doubleValue());

		/*
		 * now you have to use the method getCloneWithModifiedData giving it dataForward. Complete the following
		 * code:
		 */
		final MonteCarloAssetModel monteCarloForward =  null;

		// same thing for S_t - delta. now the new initial value will be the old one minus the step
		final Map<String, Object> dataBackward = new HashMap<String, Object>();
		dataBackward.put("initialValue", initialValue.sub(step).doubleValue());

		// final MonteCarloAssetModel monteCarloBackward =
		final MonteCarloAssetModel monteCarloBackward = null;

		/*
		 * now construct an EuropeanOption object with maturity and strike that are fields of the class,
		 * and call the getValue method in order to get something instead of null here below:
		 */
		final RandomVariable forwardOptionPayoffs = null;
		final RandomVariable backwardOptionPayoffs = null;

		/*
		 * Now, having forwardOptionPayoffs and backwardOptionPayoffs, compute RandomVariable values,
		 * which is the central difference.
		 */
		RandomVariable values = null;
		// values = (forwardOptionPayoffs-backwardOptionPayoffs)/(2*step);

		// Discounting...
		final RandomVariable numeraireAtMaturity		= underlyingSimulation.getNumeraire(maturity);
		final RandomVariable monteCarloWeights		= underlyingSimulation.getMonteCarloWeights(maturity);
		values = values.div(numeraireAtMaturity).mult(monteCarloWeights);

		// ...to evaluation time.
		final RandomVariable	numeraireAtEvalTime					= underlyingSimulation.getNumeraire(evaluationTime);
		final RandomVariable	monteCarloProbabilitiesAtEvalTime	= underlyingSimulation.
				getMonteCarloWeights(evaluationTime);
		values = values.mult(numeraireAtEvalTime).div(monteCarloProbabilitiesAtEvalTime);

		return values;
	}
}

