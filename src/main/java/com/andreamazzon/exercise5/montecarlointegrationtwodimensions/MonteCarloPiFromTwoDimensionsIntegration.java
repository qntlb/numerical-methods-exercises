package com.andreamazzon.exercise5.montecarlointegrationtwodimensions;

import java.util.function.BiFunction;

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
		BiFunction<Double, Double, Double> integrand = ((x,
				y) -> 2 * (x - 0.5) * 2 * (x - 0.5) + 2 * (y - 0.5) * 2 * (y - 0.5) <= 1 ? 4.0 : 0.0);
		this.monteCarloPiIndicator = new MonteCarloIntegrationTwoDimensions(integrand, numberOfMonteCarloComputations,
				numberOfDrawings);
		this.exactResult = Math.PI;
	}

	@Override
	protected void generateMonteCarloComputations() {
		// use of delegation through composition
		monteCarloComputations = monteCarloPiIndicator.getComputations();
	}
}