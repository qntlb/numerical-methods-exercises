package com.andreamazzon.exercise10.products;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;

/**
 * This class represents a general payoff function, through the use of the apply method
 * of RandomVariable
 *
 * @author Andrea Mazzon
 *
 */
public class GeneralOption extends AbstractAssetMonteCarloProduct {

	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
			throws CalculationException {
		// TODO Auto-generated method stub
		return null;
	}

}



