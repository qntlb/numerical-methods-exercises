package com.andreamazzon.exercise3.montecarlo;

import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;

/**
 * This is an abstract class implementing the interface MonteCarloEvaluations.
 * It provides the implementation of methods that can be called in order to get
 * the vector of several Monte-Carlo approximations of a given quantity, the
 * average and the standard deviation of the vector, as well as its minimum and
 * maximum value and an histogram of its elements. Note that all the methods are
 * based on the protected double[] field monteCarloComputations, that hosts all
 * the elements of the vector. This vector is initialized and field by the
 * protected abstract void method generateMonteCarloComputations(), whose
 * implementation depends on the kind of Monte-Carlo approximation considered.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class MonteCarloExperiments implements MonteCarloEvaluations {

	protected int numberOfMonteCarloComputations;// the length of monteCarloComputations
	// its elements are the different values obtained by the Monte-Carlo method
	protected double[] monteCarloComputations;

	// it initializes and fill the vector monteCarloComputations
	protected abstract void generateMonteCarloComputations();

	// the Javadoc documentation is already given in the interface
	@Override
	public double[] getComputations() {
		if (monteCarloComputations == null) {// generated only once!
			generateMonteCarloComputations();
		}
		return monteCarloComputations;
	}

	@Override
	public double getAverageComputations() {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.getAverage
		 */
		double average = UsefulMethodsMatricesVectors.getAverage(getComputations());
		return average;
	}

	@Override
	public double getStandardDeviationComputations() {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.getStandardDeviation
		 */
		double standardDeviation = UsefulMethodsMatricesVectors.getStandardDeviation(getComputations());
		return standardDeviation;
	}

	@Override
	public double[] getMinAndMaxComputations() {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.getMin and UsefulMethodsMatricesVectors.getMax
		 */
		double[] computations = getComputations();
		double min = UsefulMethodsMatricesVectors.getMin(computations);
		double max = UsefulMethodsMatricesVectors.getMax(computations);
		double[] minAndMax = { min, max };
		return minAndMax;
	}

	@Override
	public int[] getHistogramComputations(double leftPointOfInterval, double rightPointOfInterval, int numberOfBins) {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.buildHistogram.
		 */
		int[] histogram = UsefulMethodsMatricesVectors.buildHistogram(getComputations(), leftPointOfInterval,
				rightPointOfInterval, numberOfBins);
		return histogram;
	}
}
