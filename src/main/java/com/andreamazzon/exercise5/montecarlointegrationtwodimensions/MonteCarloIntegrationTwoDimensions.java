package com.andreamazzon.exercise5.montecarlointegrationtwodimensions;

import java.util.function.BiFunction;

import com.andreamazzon.exercise3.montecarlo.MonteCarloExperiments;

/**
 * This class deals with the Monte Carlo integration of f: [0,1]x[0,1]->R. It
 * has two private fields: the function itself, in the form of a
 * BiFunction<Double, Double, Double> object, and numberOfDrawings, i.e., the
 * number n in the approximation
 *
 * \[ 1/n \sum_{i=1}^n f(x_i,y_i) \approx \int_0^1 \int_0^1 f(x,y)dxdy. \]
 *
 * The class extends MonteCarloExperiments, so it inherits the fields
 * numberOfMonteCarloComputations and monteCarloComputations, which is the array
 * storing the computations of the Monte-Carlo integral. The array is filled by
 * the implementation of generateMonteCarloComputations().
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloIntegrationTwoDimensions extends MonteCarloExperiments {

	// Function from R^2 to R. Note the use of generics!
	private BiFunction<Double, Double, Double> integrand;

	// write the public constructor

	/**
	 * It computes the Monte Carlo approximation of the integral of integrand in
	 * [0,1].
	 *
	 * @return the approximated value of the integral
	 */
	public double computeIntegral() {
		// implement the method
		return 0;
	}

	@Override
	protected void generateMonteCarloComputations() {
		monteCarloComputations = new double[numberOfMonteCarloComputations];
		for (int i = 0; i < numberOfMonteCarloComputations; i++) {
			monteCarloComputations[i] = computeIntegral();// specific computation
		}
	}
}