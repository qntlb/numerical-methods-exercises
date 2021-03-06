package com.andreamazzon.exercise6.randomvariables;

import java.util.function.DoubleUnaryOperator;

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
	// it stores independent realizations of a function of the random variable
	private double[] randomVariableRealizationsFunction;

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
		return quantileFunction(Math.random());//X_i
	}

	@Override
	public double generate(DoubleUnaryOperator function) {
		/*
		 * same thing as before, but here we have g(X_i) with g given function,
		 * represented by a DoubleUnaryOperator. It is indeed evaluated in a realization
		 * of the random variable
		 */
		return function.applyAsDouble(generate());//f(X_i)
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

	/*
	 * This method initializes randomVariableRealizationsFunction to be a
	 * one-dimensional array of the given length n, and it fills it by calling
	 * generate(DoubleUnaryOperator function) n times. It is used to compute the
	 * mean and the standard deviation of a sample of independent realizations of
	 * the random variable.
	 */
	private void generateValues(int n, DoubleUnaryOperator function) {
		randomVariableRealizationsFunction = new double[n];
		for (int i = 0; i < n; i++) {
			randomVariableRealizationsFunction[i] = generate(function);// generation of the new realization
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

	@Override
	public double getSampleMean(int n, DoubleUnaryOperator function) {
		/*
		 * the method might be called more than once, obtaining different results. So
		 * every time the method is called we call generateValues(n, function), that is
		 * supposed to give different values to the one-dimensional array
		 * randomVariableRealizationsFunction every time is called.
		 */
		generateValues(n, function);
		double mean = UsefulMethodsMatricesVectors.getAverage(randomVariableRealizationsFunction);
		return mean;
	}

	@Override
	public double getSampleStdDeviation(int n, DoubleUnaryOperator function) {
		/*
		 * the method might be called more than once, obtaining different results. So
		 * every time the method is called we call generateValues(n), that is supposed
		 * to give different values to the one-dimensional array
		 * randomVariableRealizationsFunction every time is called.
		 */
		generateValues(n, function);
		double standardDeviation = UsefulMethodsMatricesVectors
				.getStandardDeviation(randomVariableRealizationsFunction);
		return standardDeviation;
	}

	@Override
	public double getSampleMeanWithWeightedMonteCarlo(int n, DoubleUnaryOperator function,
			RandomVariable otherRandomVariable) {
		DoubleUnaryOperator weight = x -> (densityFunction(x)// the one of the "original" random variable
				/ otherRandomVariable.densityFunction(x));
		DoubleUnaryOperator whatToSample = (x -> function.applyAsDouble(x) * weight.applyAsDouble(x));
		return otherRandomVariable.getSampleMean(n, whatToSample);
	}
}