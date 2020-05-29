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

	/*
	 * Function from R x R to R. Note the use of generics! (Look for example at the
	 * declaration of the method apply in the implementation of BiFunction)
	 */
	private BiFunction<Double, Double, Double> integrand;

	// public constructor
	public MonteCarloIntegrationTwoDimensions(BiFunction<Double, Double, Double> integrand,
			int numberOfMonteCarloComputations, int numberOfDrawings) {
		this.integrand = integrand;
		this.numberOfMonteCarloComputations = numberOfMonteCarloComputations;
		this.numberOfDrawings = numberOfDrawings;
	}

	/**
	 * It computes the Monte Carlo approximation of the integral of integrand in
	 * [0,1] x [0,1].
	 *
	 * @return the approximated value of the integral
	 */
	public double computeIntegral() {
		double integralValue = 0;
		for (int i = 0; i < numberOfDrawings; i++) {
			// every time with a different seed
			integralValue = (integralValue * i // previous average times number of previous computations
					+ integrand.apply(Math.random(), Math.random())) / (i + 1.0);
		}
		return integralValue;
	}

	@Override
	protected void generateMonteCarloComputations() {
		monteCarloComputations = new double[numberOfMonteCarloComputations];
		for (int i = 0; i < numberOfMonteCarloComputations; i++) {
			monteCarloComputations[i] = computeIntegral();// specific computation
		}
	}
}