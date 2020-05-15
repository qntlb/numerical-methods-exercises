package com.andreamazzon.exercise3.montecarlointegration;

import java.util.function.DoubleUnaryOperator;

import com.andreamazzon.exercise3.montecarlo.MonteCarloExperimentsWithExactResult;

/**
 * This class deals with Monte Carlo integration for functions of the form
 * f(x)=x^a, a>-1, x \in [0,1]. It has one private field specific of this class
 * i.e., the exponent a. This is an example of the use of composition together
 * with inheritance: it extends MonteCarloExperimentsWithExactResult, since it's
 * an application of the Monte-Carlo method where we know the exact result. So
 * all the public methods of MonteCarloExperimentsWithExactResult, and the ones
 * that MonteCarloExperimentsWithExactResult inherit from MonteCarloExperiments,
 * are inherited and already implemented. What we have to do is to implement
 * generateMonteCarloComputations. But in order to do this, we can create an
 * object of type MonteCarloIntegrationGeneralFunction giving x^a as
 * DoubleUnaryOperator in the constructor, and make it call getComputations().
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloIntegrationPowerFunction extends MonteCarloExperimentsWithExactResult {

	private MonteCarloIntegrationGeneralFunction monteCarloGeneralFunction;

	// public constructor
	public MonteCarloIntegrationPowerFunction(double exponent, int numberOfMonteCarloComputations,
			int numberOfDrawings) {
		DoubleUnaryOperator integrand = x -> Math.pow(x, exponent);
		this.monteCarloGeneralFunction = new MonteCarloIntegrationGeneralFunction(integrand,
				numberOfMonteCarloComputations, numberOfDrawings);
		this.exactResult = 1 / (1 + exponent);
	}

	@Override
	protected void generateMonteCarloComputations() {
		// use of delegation through composition
		monteCarloComputations = monteCarloGeneralFunction.getComputations();
	}
}