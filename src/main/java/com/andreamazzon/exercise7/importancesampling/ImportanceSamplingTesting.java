package com.andreamazzon.exercise7.importancesampling;

import java.util.function.DoubleUnaryOperator;

import com.andreamazzon.exercise6.randomvariables.ExponentialRandomVariable;
import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

/**
 * In this class we want to test importance sampling with weighted Monte-Carlo
 * by considering the approximation of P(X > 7) where X is an exponential random
 * variable with intensity 1. Since this is an "extreme" event and the variance
 * is very high, the quality of the approximation with standard sampling might
 * be very poor. We see that by sampling with weighted Monte-Carlo where Y is
 * normal with mean 7 the result is much better.
 *
 * @author Andrea Mazzon
 *
 */
public class ImportanceSamplingTesting {
	public static void main(String[] args) {

		int numberOfDrawings = 10000;
		/*
		 * we do 1000 tests in order to get a better idea of the difference between
		 * standard sampling and importance sampling
		 */
		int numberOfTests = 10000;

		double barrier = 7.0;// we want compute P(X > barrier)

		ExponentialRandomVariable exponential = new ExponentialRandomVariable(1.0);
		// the mean is the barrier itself!
		NormalRandomVariable shiftedNormal = new NormalRandomVariable(barrier, 1.0);
		DoubleUnaryOperator indicatorIntegrand = x -> (x > barrier) ? 1.0 : 0.0; // 1_{X > barrier}

		double analyticResult = 1 - exponential.cdfFunction(barrier);

		// we now compare the two methods

		// number of times when the error of standard sampling is lower
		int numberOffWinsStandardSampling = 0;
		// number of times when the error of importance sampling is lower
		int numberOffWinsImportanceSampling = 0;

		double averagePercentualErrorStandardSampling = 0.0;
		double averagePercentualErrorImportanceSampling = 0.0;

		for (int i = 0; i < numberOfTests; i++) {

			// standard sampling
			double resultStandardSampling = exponential.getSampleMean(numberOfDrawings, indicatorIntegrand);
			// importance sampling(weighted Monte-Carlo)
			double resultImportanceSampling = exponential.getSampleMeanWithWeightedMonteCarlo(numberOfDrawings,
					indicatorIntegrand, shiftedNormal);

			// percentage errors
			double errorStandardSampling = Math.abs(analyticResult - resultStandardSampling) / analyticResult * 100;
			double errorImportanceSampling = Math.abs(analyticResult - resultImportanceSampling) / analyticResult * 100;

			// we update the average
			averagePercentualErrorStandardSampling = (averagePercentualErrorStandardSampling * i
					+ errorStandardSampling) / (i + 1);
			averagePercentualErrorImportanceSampling = (averagePercentualErrorImportanceSampling * i
					+ errorImportanceSampling) / (i + 1);

			// we check the winner
			if (errorStandardSampling > errorImportanceSampling) {
				numberOffWinsImportanceSampling++;
			} else {
				numberOffWinsStandardSampling++;
			}
		}

		// we print the results

		System.out.println("Analytic probability of a standard double exponential variable being more than " + barrier
				+ ": " + analyticResult);

		System.out.println();

		System.out.println(
				"The average percentage error of standard sampling is " + averagePercentualErrorStandardSampling);
		// Divide by two for the double exponential coefficient!

		System.out.println(
				"The average percentage error of importance sampling is " + averagePercentualErrorImportanceSampling);

		System.out.println();

		System.out.println("Number of times when importance sampling is better: " + numberOffWinsImportanceSampling);
		System.out.println("Number of times when standard sampling is better: " + numberOffWinsStandardSampling);

	}
}
