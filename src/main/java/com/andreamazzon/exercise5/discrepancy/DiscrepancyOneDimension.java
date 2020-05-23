package com.andreamazzon.exercise5.discrepancy;

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
		// return the discrepancy if set. If you want you can use the method below
		return 0;
	}

	/*
	 * Returns max_{b \in set} max(|{x_i in [set[position],b]}|/n-(b-set[position]),
	 * (b-set[position])-|{x_i \in (set[position],b)}|/n), where set is the
	 * one-dimensional array of the points of the set whose discrepancy must be
	 * computed, n is the length of set, and x_i are points of the set.
	 */
	private static double getMaximumValue(double[] set, int position) {
		// implement the method
		return 0;
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
		// return the star discrepancy of set
		return 0;
	}
}
