package com.andreamazzon.exercise8.brownianmotion;

import java.text.DecimalFormat;

import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class tests some features of Brownian motion through Monte Carlo
 * simulation
 *
 * @author Andrea Mazzon
 *
 */
public class BrownianChecks {

	public static void main(String[] args) {
		final DecimalFormat formatterValue = new DecimalFormat(" ##0.00000;" + "-##0.00000");
		final DecimalFormat formatterTime = new DecimalFormat(" ##0.00;");

		final int numberOfFactors = 2;
		final int numberOfPaths = 10000;

		//time discretization parameters
		final double initialTime = 0.0;
		final int numberOfTimeSteps = 100;
		final double timeHorizon = 1.0;
		final double deltaT = timeHorizon / numberOfTimeSteps;

		// time discretization from the Finmath library
		final TimeDiscretization timeDiscretization = new TimeDiscretizationFromArray(
				initialTime, numberOfTimeSteps, deltaT);

		// we construct the Brownian motion object
		final MyBrownianMotion brownianMotion = new MyBrownianMotion(timeDiscretization,
				numberOfFactors,numberOfPaths);
		// or
		//MyBrownianMotion brownianMotion = new MyBrownianMotion(initial, numberOfTimeSteps, deltaT,
		// numberOfFactors, numberOfPaths);

		/*
		 * we first want to compute an approximation of the correlation between the two
		 * factors, at ever time t_i in the discretization. This is the approximation of
		 * E[B_{t_j}^1B_{t_j}^2]-E[B_{t_j}^1]E[B_{t_j}^2] = E[B_{t_j}^1B_{t_j}^2]. So we
		 * have to compute the approximated expectation of B_{t_j}^1B_{t_j}^2.
		 */
		final double[] averagesOfTheProduct = new double[numberOfTimeSteps + 1];

		/*
		 * we compute the product across all the paths, yielding a new RandomVariable
		 * object, and then compute its average. We use the mult(RandomVariable
		 * randomVariable) and getAverage methods of the RandomVariableFromDoubleArray
		 * class
		 */
		for (int timeIndex = 0; timeIndex < averagesOfTheProduct.length; timeIndex += 10) {

			averagesOfTheProduct[timeIndex] = brownianMotion.getSimulations(timeIndex, 0) // RandomVariableFromDoubleArray
					.mult(brownianMotion.getSimulations(timeIndex, 1)) // multiply with RandomVariableFromDoubleArray
					.getAverage(); // and get the average

			System.out.println("The correlation of the two factors at time " +
					formatterTime.format(timeDiscretization.getTime(timeIndex)) + " is " + " "
					+ formatterValue.format(averagesOfTheProduct[timeIndex]));
		}

		System.out.println();

		// we now compute the cross correlation E[B^1_{t_j}B^1_{t_i}], for t_i != t_j

		/*
		 * Suppose t>s. Then it holds
		 * E[B_tB_s] = E[(B_t-B_s)B_s+B_s^2] = E[B_t - B_s]E[B_s] + E[B_s^2] = s,
		 * where the second equality comes from the independence of the increments
		 * of Brownian motion.
		 */

		final double firstTime = 0.1;
		final double secondTime = 0.2;

		/*
		 * we want now to get the index corresponding to the given times. If the times
		 * are not part of the time discretization, it returns a negative number.
		 */
		final int crossCorrelationTestIndex1 = timeDiscretization.getTimeIndex(firstTime);
		final int crossCorrelationTestIndex2 = timeDiscretization.getTimeIndex(secondTime);

		// computes the cross correlation at certain times for the first Factor
		final double crossCorrelationTest = brownianMotion.getSimulations(crossCorrelationTestIndex1,0)
				.mult(brownianMotion.getSimulations(crossCorrelationTestIndex2, 0))
				.getAverage();

		System.out.println("The serial correlation at times " + firstTime + " and " + secondTime + " is " + " "
				+
				crossCorrelationTest);

		System.out.println();

		/*
		 * pathwise analysis: we want to check that \int_0^T W_sdW_s = (W_T^2- T)/2, by
		 * approximating the integral
		 */
		final int samplePath = 2;

		/*
		 * we compute the two sides at final time, so with index numberOfTimeSteps =
		 * numberOfTimes - 1. Pathwise: here we have the normal product between doubles
		 */
		final double brownianMotionAtFinalTimeAtGivenPath = brownianMotion.getSimulations(numberOfTimeSteps, 0)
				.get(samplePath);
		final double rightHandSide = 0.5 * (brownianMotionAtFinalTimeAtGivenPath * brownianMotionAtFinalTimeAtGivenPath
				- numberOfTimeSteps * deltaT);

		// here we approximate the integral with respect to time
		double leftHandSide = 0.0;
		for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
			leftHandSide += brownianMotion.getSimulations(timeIndex, 0).get(samplePath)
					* brownianMotion.getBrownianIncrement(timeIndex, 0).get(samplePath);
		}

		System.out.println();

		System.out.println(
				"Int_0^T W_t dW_t is approximated by " + leftHandSide + " while (W^2_t-t)/2 equals " +
						rightHandSide);

	}
}
