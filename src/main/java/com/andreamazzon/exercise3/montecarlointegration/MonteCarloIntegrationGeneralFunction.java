package com.andreamazzon.exercise3.montecarlointegration;

import java.util.function.DoubleUnaryOperator;

import com.andreamazzon.exercise3.montecarlo.MonteCarloExperiments;

/**
 * This class deals with Monte Carlo integration of general functions in the
 * interval [0,1]. It has two private fields: the function itself, in the form
 * of a DoubleUnaryOperator object, and numberOfDrawings, i.e., the number n in
 * the approximation
 *
 * \[ 1/n \sum_{i=1}^n f(x_i) \approx \int_0^1 f(x)dx. \]
 *
 * The class extends MonteCarloExperiments, so it inherits the fields
 * numberOfMonteCarloComputations and monteCarloComputations, which is the array
 * storing the computations of the Monte-Carlo integral. The array is filled by
 * the implementation of generateMonteCarloComputations().
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloIntegrationGeneralFunction extends MonteCarloExperiments {

	private DoubleUnaryOperator integrand;
	private int numberOfDrawings;

	// public constructor
	public MonteCarloIntegrationGeneralFunction(DoubleUnaryOperator integrand, int numberOfMonteCarloComputations,
			int numberOfDrawings) {
		this.integrand = integrand;
		this.numberOfMonteCarloComputations = numberOfMonteCarloComputations;
		this.numberOfDrawings = numberOfDrawings;
	}

	/**
	 * It computes the Monte Carlo approximation of the integral of integrand in
	 * [0,1].
	 *
	 * @return the approximated value of the integral
	 */
	public double computeIntegral() {
		double integralValue = 0;
		/*
		 * another (more efficient) way would be to do it with DoubleStream, look for
		 * example at
		 * info.quantlab.numericalmethods.lecture.montecarlo.MonteCarloIntegrator. Let's
		 * see this one here to see this way to compute the average
		 */
		for (int i = 0; i < numberOfDrawings; i++) {
			// every time with a different seed
			integralValue = (integralValue * i + integrand.applyAsDouble(Math.random())) / (i + 1.0);
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