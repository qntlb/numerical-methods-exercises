package com.andreamazzon.exercise10.products;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;

/**
 * This class represents an exchange option, involving therefore a two-dimensional process
 *
 * @author Andrea Mazzon
 *
 */
public class ExchangeOption extends AbstractAssetMonteCarloProduct {

	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
			throws CalculationException {
		// TODO Auto-generated method stub
		return null;
	}

}
