package com.andreamazzon.exercise6.randomvariables;

/**
 * This class represents exponential random variables. It inherits from
 * RandomVariable, and gives the implementation of the methods depending
 * directly on the distribution.
 *
 * @author Andrea Mazzon
 *
 */
public class ExponentialRandomVariable extends RandomVariable {
	private double lambda; // intensity

	/*
	 * public constructor, and no public setters: every object will have its own
	 * intensity.
	 */
	public ExponentialRandomVariable(double lambda) {
		this.lambda = lambda;// intensity
	}

	public double getLambda() { // getter, if the user wants to get the intensity
		return lambda;
	}

	// The following methods are specific of the exponential random variable

	@Override
	public double getAnalyticMean() {
		return 1.0 / lambda;
	}

	@Override
	public double getAnalyticStdDeviation() {
		return 1.0 / lambda;

	}

	@Override
	public double densityFunction(double x) {
		return lambda * Math.exp(-lambda * x);
	}

	@Override
	public double cdfFunction(double x) {
		return (1 - Math.exp(-lambda * x));
	}

	@Override
	public double quantileFunction(double x) {
		/*
		 * The distribution function is continuous and increasing, so we just have to
		 * find y such that F(y) = x. We have F(y) = 1 - e^ (-lambda * y) = x, therefore
		 * e^ (-lambda * y) = 1 - x ---> y = - log(1-x)/lambda
		 */
		return -Math.log(1 - x) / lambda;
	}
}