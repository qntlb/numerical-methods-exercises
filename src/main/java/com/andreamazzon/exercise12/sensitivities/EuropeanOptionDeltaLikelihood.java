package com.andreamazzon.exercise12.sensitivities;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;

/**
 * This class implements the computation of the delta of an European call option for a Black
 * Scholes model, through the Likelihood Ratio Method. Note that the only overridden method is getValue.
 *
 * @author Andrea Mazzon

 */
public class EuropeanOptionDeltaLikelihood extends AbstractAssetMonteCarloProduct {

	private final double maturity;
	private final double strike;

	public EuropeanOptionDeltaLikelihood(double maturity, double strike) {
		super();
		this.maturity = maturity;
		this.strike = strike;
	}

	/**
	 * This method returns the value of the delta of an European call option through the Likelihood
	 * Ratio Method, as a RandomVariable. Note that the model has to be a Black Scholes model.
	 *
	 * @param evaluationTime The time on which this products value should be observed.
	 * @param monteCarloModel, object of type AssetModelMonteCarloSimulationModel used to price the
	 * product. Its model has to be Black Scholes, otherwise an exception is thrown.
	 * @return The random variable representing the value of the delta discounted to evaluation time
	 * @throws net.finmath.exception.CalculationException Thrown if the valuation fails, specific
	 * cause may be available via the <code>cause()</code> method.
	 */
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel
			monteCarloModel) throws CalculationException {
		/*
		 * the model has to be Black Scholes! The implementation of the method (i.e., the
		 * computation of the derivative of the density) is specific to the Black Scholes model.
		 */
		BlackScholesModel blackScholesModel;
		try {
			//is the model got from the getModel() method aan object of the BlackScholes class?
			blackScholesModel = (/*downcast*/BlackScholesModel)
					(/*downcast*/(MonteCarloAssetModel) monteCarloModel).getModel();
		}
		catch(final Exception e) {
			throw new ClassCastException("This method requires a Black-Scholes type model"
					+ "(MonteCarloBlackScholesModel).");
		}

		/*
		 * first step: get the underlying at maturity S(T), the current value of the underlying,
		 * the risk free rate and the volatility sigma. This can be done from monteCarloModel and
		 * blackScholesModel.
		 */
		// Get underlying and numeraire
		final RandomVariable underlyingAtMaturity	= monteCarloModel.getAssetValue(maturity, 0);
		final RandomVariable underlyingAtToday = monteCarloModel.getAssetValue(evaluationTime, 0);

		final RandomVariable riskFree = blackScholesModel.getRiskFreeRate();
		final RandomVariable sigma = blackScholesModel.getVolatility();

		/*
		 * second step: use the quantities found above in order to compute the delta. Use the formulas
		 * at page 432 of the script, together with page 429. Once again, values has to represent
		 * the delta.
		 */

		/*
		 * We have to compute (d Phi_{S(T)}(S(T))/d S_0)/Phi_{S(T)}(S(T)),
		 * with
		 * Phi_{S(T)}(S(T)) =
		 * 1/(sigma sqrt(T)) Phi_{std.norm}( 1/(sigma sqrt(T)) ( log(S_T/S_0)-rT+1/2 sigma^2T )/S_T ),
		 * 	Phi_{std_norm}(x)=1/sqrt(2 pi)*exp(-x^2/2), d/dx Phi_{std_norm}(x) = - x Phi_{std_norm}(x)
		 * So we have
		 * d Phi_{S(T)}(S(T))/d S_t = 1/(sigma sqrt(T)) * (-1/(sigma sqrt(T))(log(S_T/S_t)-rT+1/2 sigma^2T)/S_T)(-1/S_t)
		 */

		final RandomVariable likelihoodRatio = underlyingAtMaturity.div(underlyingAtToday).log().
				sub(riskFree.mult(maturity)).add(sigma.squared().mult(0.5*maturity)).div(sigma).div(sigma)
				.div(maturity).div(underlyingAtToday);

		RandomVariable values	= underlyingAtMaturity.sub(strike).floor(0.0).mult(likelihoodRatio);

		// Discounting...
		final RandomVariable numeraireAtMaturity = monteCarloModel.getNumeraire(maturity);
		final RandomVariable monteCarloWeights = monteCarloModel.getMonteCarloWeights(maturity);
		values = values.div(numeraireAtMaturity).mult(monteCarloWeights);

		// ...to evaluation time.
		final RandomVariable	numeraireAtEvalTime	= monteCarloModel.getNumeraire(evaluationTime);
		final RandomVariable	monteCarloProbabilitiesAtEvalTime = monteCarloModel.
				getMonteCarloWeights(evaluationTime);
		values = values.mult(numeraireAtEvalTime).div(monteCarloProbabilitiesAtEvalTime);

		return values;
	}
}

