package com.andreamazzon.exercise5.discrepancy;

import java.util.Arrays;

/**
 * This class has a main method where we compute the discrepancy and star
 * discrepancy of two sets and where we print the star discrepancy of Van der
 * Corput sequences of increasing length n, together with log(n)/n.
 *
 * @author Andrea Mazzon
 *
 */
public class MainClass {

	public static void main(String[] args) throws Exception {

		// first we compute discrepancy and star discrepancy of some sets..

		double[] firstSet = { 0.125, 0.25, 0.5, 0.75 };
		System.out.println("The discrepancy of the set " + Arrays.toString(firstSet) + " is "
				+ DiscrepancyOneDimension.getDiscrepancy(firstSet));
		System.out.println("The star discrepancy of the set " + Arrays.toString(firstSet) + " is "
				+ DiscrepancyOneDimension.getStarDiscrepancy(firstSet));

		double[] secondSet = { 0.2, 0.21, 0.22, 0.23, 0.24, 0.65, 0.76, 0.87 };
		System.out.println("The discrepancy of the set " + Arrays.toString(secondSet) + " is "
				+ DiscrepancyOneDimension.getDiscrepancy(secondSet));
		System.out.println("The star discrepancy of the set " + Arrays.toString(secondSet) + " is "
				+ DiscrepancyOneDimension.getStarDiscrepancy(secondSet));

		// in the computation of the discrepancy we are not checking [0.27, 1]!
		double[] thirdSet = { 0.1, 0.27 };
		System.out.println("The discrepancy of the set " + Arrays.toString(thirdSet) + " is "
				+ DiscrepancyOneDimension.getDiscrepancy(thirdSet));
		System.out.println("The star discrepancy of the set " + Arrays.toString(thirdSet) + " is "
				+ DiscrepancyOneDimension.getStarDiscrepancy(thirdSet));

		// the we plot the star discrepancies of Van der Corput
		int maxSequenceSize = 1000;
		int base = 2;
		VanDerCorputDiscrepancy.plotVanDerCorputStarDiscrepancy(maxSequenceSize, base);

	}

}
