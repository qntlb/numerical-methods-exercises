package com.andreamazzon.exercise5.montecarlointegrationtwodimensions;

import java.util.Arrays;

import com.andreamazzon.session4.usefulmatrices.DifferentLengthException;

public class MonteCarloPiFromTwoDimensionsIntegrationsCheck {

	public static void main(String[] args) {

		int numberOfIntegrations = 100;// number of Monte Carlo executions
		int numberOfDrawings = 100000;

		MonteCarloPiFromTwoDimensionsIntegration simulator = new MonteCarloPiFromTwoDimensionsIntegration(
				numberOfIntegrations, numberOfDrawings);

		// mean and variance of the realizations
		double averageComputations = simulator.getAverageComputations();
		double standardDeviationComputation = simulator.getStandardDeviationComputations();

		System.out.println(
				"Mean of the realisations of pi for  " + numberOfDrawings + " drawings: " + averageComputations);

		System.out.println("Standard Deviation of the realisations of pi for  " + numberOfDrawings + " drawings: "
				+ standardDeviationComputation);

		System.out.println();

		/*
		 * now we compute the average of the error with respect to the value of Pi
		 * produced by Java.
		 */
		try {
			/*
			 * you want to be sure that the array of the approximations has same length as
			 * the array filled with the exact solution. Otherwise an exception is thrown
			 */
			double averageAbsoluteError = simulator.getAverageAbsoluteError();
			System.out.println("Average of the errors in the computation of Pi with " + numberOfDrawings + " drawings: "
					+ averageAbsoluteError);
		} catch (DifferentLengthException exception) {
			exception.print();
		}

		System.out.println();

		// left point of the interval considered in the histogram
		double leftPointHistogram = Math.PI - Math.PI / 100;
		// right point of the interval considered in the histogram
		double rightPointHistogram = Math.PI + Math.PI / 100;

		int binsNumber = 11;// plus 2 for outliers

		int[] bins = simulator.getHistogramComputations(leftPointHistogram, rightPointHistogram, binsNumber);

		System.out.println("Vector for the histogram of the computions of pi: " + Arrays.toString(bins));
		System.out.println("\n");

		System.out.println("Mean of the errors for different number of drawings: ");

		System.out.println();

		numberOfDrawings = 10;

		while (numberOfDrawings <= 1000000) {
			MonteCarloPiFromTwoDimensionsIntegration newSimulator = new MonteCarloPiFromTwoDimensionsIntegration(
					numberOfIntegrations, numberOfDrawings);
			try {
				/*
				 * you want to be sure that the array of the approximations has same length as
				 * the array filled with the exact solution. Otherwise an exception is thrown
				 */
				double averageAbsoluteError = newSimulator.getAverageAbsoluteError();
				System.out.println("Mean of the errors in the computation of Pi with " + numberOfDrawings
						+ " drawings: " + averageAbsoluteError);
			} catch (DifferentLengthException exception) {
				exception.print();
			}
			numberOfDrawings *= 10;
		}
	}

}
