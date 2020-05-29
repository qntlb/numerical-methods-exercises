package com.andreamazzon.exercise4;

/**
 * This class is used in order to compute the elements of a Van der Corput
 * sequence with a given base. It consists of a static method.
 *
 * @author Andrea Mazzon, from the implementation of Christian Fries
 *
 */
public class VanDerCorputSequence {

	/**
	 * @param index The index of the sequence starting with 0
	 * @param base  The base.
	 * @return The van der Corput number
	 */
	public static double getVanDerCorputNumber(long index, int base) {

		index = index + 1;// because we start from zero

		double x = 0.0;
		// first refinement
		double refinementFactor = 1.0 / base;

		while (index > 0) {
			x += (index % base) * refinementFactor;
			index = index / base;
			refinementFactor = refinementFactor / base;
		}

		return x;
	}

	/**
	 * @param index The length of the sequence starting with 0
	 * @param base  The base.
	 * @return The van der Corput sequence up to the index n
	 */
	public static double[] getVanDerCorputSequence(int n, int base) {
		double[] sequence = new double[n];
		for (int i = 0; i <= n - 1; i++) {
			sequence[i] = getVanDerCorputNumber(i, base);
		}
		return sequence;
	}
}
