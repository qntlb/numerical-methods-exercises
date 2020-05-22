package com.andreamazzon.exercise3.montecarlo;

import java.util.Arrays;

import com.andreamazzon.session4.usefulmatrices.DifferentLengthException;
import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;

/**
 * This is an abstract class inheriting from MonteCarloExperiments, so also
 * implementing the interface MonteCarloEvaluations. Therefore, it provides the
 * implementation of methods that can be called in order to get the vector of
 * several Monte-Carlo approximations of a given quantity, the average and the
 * standard deviation of the vector, as well as its minimum and maximum value
 * and an histogram of its elements. Moreover, here we suppose that we already
 * know the exact value of the quantity we approximate by Monte-Carlo. So we
 * experiment on the quality of our approximation by getting the vector of the
 * absolute errors and the average of the vector. This is done by the methods
 * double[] getAbsoluteErrorsOfComputations() and double
 * getAverageAbsoluteError(), respectively. Note that all the methods are based
 * on the protected double[] field monteCarloComputations, inherited from
 * MonteCarloExperiments, that hosts all the elements of the vector. This vector
 * is initialized and field by the protected abstract void method
 * generateMonteCarloComputations(), also inherited from MonteCarloExperiments,
 * whose implementation depends on the kind of Monte-Carlo approximation
 * considered.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class MonteCarloExperimentsWithExactResult extends MonteCarloExperiments {

	protected double exactResult;// hosts the exact value of the quantity we approximate

	/**
	 * It returns the vector of the absolute errors of the Monte-Carlo
	 * approximations
	 *
	 * @return the vector of the absolute errors of the Monte-Carlo approximations,
	 *         computed as the difference between the array of the approximation and
	 *         an array filled with the exact result.
	 * @throws DifferentLengthException, if the length of the array of Monte-Carlo
	 *                                   approximations (i.e., the array returned by
	 *                                   getComputations()) is different from the
	 *                                   one of the array that we fill with the
	 *                                   exact result.
	 */
	public double[] getAbsoluteErrorsOfComputations() throws DifferentLengthException {
		double[] computations = getComputations();
		double[] arrayWithExactResult = new double[computations.length];
		Arrays.fill(arrayWithExactResult, exactResult);// Java method!
		double[] errors = UsefulMethodsMatricesVectors.differenceVectors(computations, arrayWithExactResult);
		double[] absoluteErrors = UsefulMethodsMatricesVectors.absVector(errors);
		return absoluteErrors;
	}

	/**
	 * It returns the average of the vector of the absolute errors of the
	 * Monte-Carlo approximations
	 *
	 * @return the average of the vector of the absolute errors of the Monte-Carlo
	 *         approximations. The vector is computed as the difference between the
	 *         array of the approximation and an array filled with the exact result.
	 * @throws DifferentLengthException, if the length of the array of Monte-Carlo
	 *                                   approximations (i.e., the array returned by
	 *                                   getComputations()) is different from the
	 *                                   one of the array that we fill with the
	 *                                   exact result.
	 */
	public double getAverageAbsoluteError() throws DifferentLengthException {
		return UsefulMethodsMatricesVectors.getAverage(getAbsoluteErrorsOfComputations());
	}

}
