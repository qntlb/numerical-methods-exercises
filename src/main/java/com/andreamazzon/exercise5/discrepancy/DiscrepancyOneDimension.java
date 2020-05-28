package com.andreamazzon.exercise5.discrepancy;

import java.util.Arrays;

/**
 * This class is devoted to the computation of the discrepancy and of the star
 * discrepancy of a set of points in one dimension. The points of the set are
 * given as a one-dimensional array, which is not supposed to be already sorted
 * when passed in the argument list of the methods.
 *
 * @author Andrea Mazzon
 *
 */
public class DiscrepancyOneDimension {

	/**
	 * It computes and return the discrepancy of a set of points in one dimension.
	 * In particular, the discrepancy of the one-dimensional set is computed as
	 * max_{a \in set} (max_{b \in set} max(|{x_i \in [a,b]}|/n - (b-a), (b-a)-|{x_i
	 * in (a,b)}|/n)), where set is the one-dimensional array of the points of the
	 * set whose discrepancy must be computed, n is the length of the array, and x_i
	 * are points of the array. So we can check intervals [set[position],b], where
	 * position runs from 0 (first element of the set) to totalNumberOfPoints - 1
	 * (second last) and b is bigger than set[position]
	 *
	 * @param set, a one-dimensional array giving the points of the set whose
	 *             discrepancy must be computed. It is not supposed to be already
	 *             sorted when passed in the argument list, so it must be sorted at
	 *             the beginning by the Java method Arrays.sort.
	 */
	public static double getDiscrepancy(double[] set) {
		Arrays.sort(set); // Java method to sort the set
		final int totalNumberOfPoints = set.length;
		/*
		 * we first get the star discrepancy, i.e., we check intervals [0,b], where b
		 * varies in the set of points.
		 */
		double discrepancy = getStarDiscrepancy(set);
		/*
		 * we now check open and closed intervals from set[position] to b, where
		 * position runs from 0 (first element of the set) to totalNumberOfPoints - 2
		 * (second last, we don't consider the last element in the set for the reason
		 * you can see in the PDF) and b is bigger than set[position]
		 */
		for (int position = 0; position <= totalNumberOfPoints - 2; position++) {
			/*
			 * maximum value of the absolute value that appears in the definition of
			 * discrepancy, given by intervals whose left end is set[position]
			 */
			final double newCandidate = getMaximumValue(set, position);
			/*
			 * if this new value is higher than the current maximum, we update the current
			 * maximum
			 */
			discrepancy = Math.max(discrepancy, newCandidate);
		}
		return discrepancy;
	}

	/*
	 * Returns max_{b \in set} max(|{x_i in [set[position],b]}|/n-(b-set[position]),
	 * (b-set[position])-|{x_i \in (set[position],b)}|/n), where set is the
	 * one-dimensional array of the points of the set whose discrepancy must be
	 * computed, n is the length of set, and x_i are points of the set.
	 */
	private static double getMaximumValue(double[] set, int position) {
		final int totalNumberOfPoints = set.length;
		// they will get incremented by one every time we make b run in the set
		double numberOfPointsInTheOpenIntervals = 0;
		double numberOfPointsInTheClosedIntervals = 2;
		double discrepancy = 0;
		for (int i = 1; i <= totalNumberOfPoints - position - 1; i++) {
			final double lengthOfNewInterval = set[position + i] - set[position];
			final double newCandidate = Math.max(
					lengthOfNewInterval - numberOfPointsInTheOpenIntervals / totalNumberOfPoints,
					numberOfPointsInTheClosedIntervals / totalNumberOfPoints - lengthOfNewInterval);
			// we update the maximum
			discrepancy = Math.max(discrepancy, newCandidate);
			numberOfPointsInTheOpenIntervals++;
			numberOfPointsInTheClosedIntervals++;
		}
		// now we check the set involving b=1
		if (set[totalNumberOfPoints - 1] != 1) {
			final double lengthOfNewInterval = 1 - set[position];
			/*
			 * this is the only one that can increase the discrepancy: for closed sets, you
			 * have same number of points with bigger length of the interval
			 */
			final double newCandidate = lengthOfNewInterval - numberOfPointsInTheOpenIntervals / totalNumberOfPoints;
			discrepancy = Math.max(discrepancy, newCandidate);
		}
		return discrepancy;
	}

	/**
	 * It computes and return the star discrepancy of a set of points in one
	 * dimension. The star discrepancy of the set of points set is computed as
	 * max_{b \in set} max (b - |{x_i \in (0,b)}|/n), |{x_i \in [0,b]}|/n -b ),
	 * where set is the one-dimensional array of the points of the set whose
	 * discrepancy must be computed, n is the length of the array, and x_i are
	 * points of the array.
	 *
	 * @param set, a one-dimensional array giving the points of the set whose
	 *             discrepancy must be computed. It is not supposed to be already
	 *             sorted when passed in the argument list, so it must be sorted at
	 *             the beginning by the Java method Arrays.sort.
	 */
	public static double getStarDiscrepancy(double[] set) {
		final int totalNumberOfPoints = set.length;
		Arrays.sort(set); // Java method to sort the set
		double starDiscrepancy = 0;
		double numberOfPointsInTheClosedIntervals = 1;
		for (int i = 0; i < totalNumberOfPoints; i++) {
			final double newCandidate = Math.max(
					set[i] - (numberOfPointsInTheClosedIntervals - 1) / totalNumberOfPoints,
					numberOfPointsInTheClosedIntervals / totalNumberOfPoints - set[i]);
			// we update the maximum
			starDiscrepancy = Math.max(starDiscrepancy, newCandidate);
			numberOfPointsInTheClosedIntervals++;
		}
		// note: no need to check the pint 1: it gives value 0
		return starDiscrepancy;
	}
}
