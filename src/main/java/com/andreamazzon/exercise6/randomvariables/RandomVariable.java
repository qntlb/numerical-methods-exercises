package com.andreamazzon.exercise6.randomvariables;

import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;

/**
 * This is an abstract class implementing RandomVariableInterface: the
 * distribution of the random variable is not specified here yet, but the
 * implementation is given of methods that can be written without knowing the
 * specific distribution. These methods are generate(), that can be implemented
 * by calling quantileFunction (whose implementation will be given in classes
 * inheriting from this one) evaluated at Math.random(), and the two methods
 * getSampleMean(int n), getSampleStdDeviation(int n). These depend on a
 * one-dimensional array randomVariableRealizations which is filled by calling
 * generate() n times.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class RandomVariable implements RandomVariableInterface {
	// it stores independent realizations of the random variable
	private double[] randomVariableRealizations;

	@Override
	public abstract double cdfFunction(double x);// P(X <= x)

	@Override
	public abstract double densityFunction(double x);// derivative of cdf

	@Override
	public abstract double quantileFunction(double x);// inf{y|cdf(y)>=x}

	@Override
	public abstract double getAnalyticMean();// depends on the specific distribution!

	@Override
	public abstract double getAnalyticStdDeviation();// depends on the specific distribution!

	@Override
	public double generate() {
		/*
		 * Inversion of the distribution function: here we use the fact that X :=
		 * F ^(-1)(U) with U uniformly distributed in (0,1)) and F^(-1) defined as
		 * F ^(-1)(y) := inf{x|F(x) >=  y} has cumulative distribution function F.
		 * F^(-1) is here the quantile function of the random variable. The
		 * implementation of quantileFunction(double x) will be given in the classes
		 * extending this abstract one, since of course it depends on the specific
		 * distribution.
		 */
		return quantileFunction(Math.random());
	}

	/*
	 * This method initializes randomVariableRealizations to be a one-dimensional
	 * array of the given length n, and it fills it by calling generate() n times.
	 * It is used to compute the mean and the standard deviation of a sample of
	 * independent realizations of the random variable.
	 */
	private void generateValues(int n) {
		randomVariableRealizations = new double[n];
		for (int i = 0; i < n; i++) {
			randomVariableRealizations[i] = generate();// generation of the new realization
		}
	}

	@Override
	public double getSampleMean(int n) {
		/*
		 * the method might be called more than once, obtaining different results. So
		 * every time the method is called we call generateValues(n), that is supposed
		 * to give different values to the one-dimensional array
		 * randomVariableRealizations every time is called.
		 */
		generateValues(n);
		double mean = UsefulMethodsMatricesVectors.getAverage(randomVariableRealizations);
		return mean;
	}

	@Override
	public double getSampleStdDeviation(int n) {
		/*
		 * the method might be called more than once, obtaining different results. So
		 * every time the method is called we call generateValues(n), that is supposed
		 * to give different values to the one-dimensional array
		 * randomVariableRealizations every time is called.
		 */
		generateValues(n);
		double standardDeviation = UsefulMethodsMatricesVectors.getStandardDeviation(randomVariableRealizations);
		return standardDeviation;
	}
}