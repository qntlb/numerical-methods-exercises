package com.andreamazzon.exercise5.montecarlointegrationtwodimensions;

import com.andreamazzon.exercise3.montecarlo.MonteCarloExperimentsWithExactResult;

/**
 * This class deals with the approximation of Pi from the area of the unit
 * circle: \[ 1/n \sum_{i=1}^n 1_{(2(x_i-0.5))^2+(2(y_i-0.5))^2 <=1} \approx
 * \int_0^1\int_0^1 1_{(2(x-0.5))^2+(2(y-0.5))^2 <=1} dxdy. \] The
 * implementation of the above approximation is delegated to an object of type
 * MonteCarloPiFromTwoDimensionsIntegration.
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloPiFromTwoDimensionsIntegration extends MonteCarloExperimentsWithExactResult {

	private MonteCarloIntegrationTwoDimensions monteCarloPiIndicator;

	// public constructor
	public MonteCarloPiFromTwoDimensionsIntegration(int numberOfMonteCarloComputations, int numberOfDrawings) {
		/*
		 * give the implementation of the constructor. You have to give a value to the
		 * fields exactResult and monteCarloPiIndicator. If you don't know how to
		 * initialize a BiFunction<Double, Double, Double> object, here is an example:
		 * BiFunction<Double, Double, Double> function = ((x, y) -> x * y represents
		 * f(x,y)=x*y
		 */
	}

	@Override
	protected void generateMonteCarloComputations() {
		// give the implementation of this method
	}
}