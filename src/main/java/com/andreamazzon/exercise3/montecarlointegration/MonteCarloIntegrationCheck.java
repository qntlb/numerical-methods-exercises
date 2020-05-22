package com.andreamazzon.exercise3.montecarlointegration;

import java.util.Arrays;

import com.andreamazzon.session4.usefulmatrices.DifferentLengthException;

public class MonteCarloIntegrationCheck {
	public static void main(String[] args) {

		int numberOfIntegrations = 100;// number of Monte Carlo executions
		int numberOfDrawings = 100000;

		double exponent = 2;

		MonteCarloIntegrationPowerFunction simulator = new MonteCarloIntegrationPowerFunction(exponent,
				numberOfIntegrations, numberOfDrawings);

		// mean and variance of the realizations
		double averageComputations = simulator.getAverageComputations();
		double standardDeviationComputation = simulator.getStandardDeviationComputations();

		System.out.println("Mean of the approximations of the integral for  " + numberOfDrawings + " drawings: "
				+ averageComputations);

		System.out.println("Standard Deviation of the approximations of the integral for  " + numberOfDrawings
				+ " drawings: " + standardDeviationComputation);

		System.out.println();

		/*
		 * now we compute the average of the error with respect to the analytical value
		 * of the integral
		 */
		try {
			/*
			 * you want to be sure that the array of the approximations has same length as
			 * the array filled with the exact solution. Otherwise an exception is thrown
			 */
			double averageAbsoluteError = simulator.getAverageAbsoluteError();
			System.out.println("Mean of the errors in the computation of the integral with " + numberOfDrawings
					+ " drawings: " + averageAbsoluteError);
		} catch (DifferentLengthException exception) {
			exception.print();
		}

		System.out.println();

		double exactResult = 1 / (1 + exponent);

		// left point of the interval considered in the histogram
		double leftPointHistogram = exactResult - exactResult / 100;
		// right point of the interval considered in the histogram
		double rightPointHistogram = exactResult + exactResult / 100;

		int binsNumber = 11;// plus 2 for outliers

		int[] bins = simulator.getHistogramComputations(leftPointHistogram, rightPointHistogram, binsNumber);

		System.out.println("Vector for the histogram of the approximations of the integral: " + Arrays.toString(bins));
		System.out.println("\n");

		System.out.println("Mean of the errors for different number of drawings: ");

		System.out.println();

		numberOfDrawings = 10;

		while (numberOfDrawings <= 10000000) {
			MonteCarloIntegrationPowerFunction newSimulator = new MonteCarloIntegrationPowerFunction(exponent,
					numberOfIntegrations, numberOfDrawings);
			try {
				/*
				 * you want to be sure that the array of the approximations has same length as
				 * the array filled with the exact solution. Otherwise an exception is thrown
				 */
				double averageAbsoluteError = newSimulator.getAverageAbsoluteError();
				System.out.println("Mean of the errors in the approsimation of the integral with " + numberOfDrawings
						+ " drawings: " + averageAbsoluteError);
			} catch (DifferentLengthException exception) {
				exception.print();
			}
			numberOfDrawings *= 10;
		}
	}
}
