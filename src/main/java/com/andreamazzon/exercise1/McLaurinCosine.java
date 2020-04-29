package com.andreamazzon.exercise1;

/**
 * This class deals with the Maclaurin series for the cosine in one point. Note
 * that the Maclaurin series are an expansion of a function around zero.
 * However, it can be seen that it works well for every number if opportunely
 * reduced to the domain [0, 2 pi). We can note that when computing the
 * expansion, an overflow can be caused by both the terms x^(2i) and the
 * factorial of i. If i is an integer, the factorial may give negative numbers,
 * that can lead to bugs most of all if x is large (outside [0,2 pi). It gives
 * instead Infinity, which results in a zero term when we divide the power of x
 * by the factorial, if i is a double and x and i are not too large (otherwise,
 * x^(2i) is also Infinity and we get a NaN).
 * 
 * @author Andrea Mazzon
 *
 */
public class McLaurinCosine {
	/**
	 * It computes the Maclaurin series of the cosine in a point x, supposed to be
	 * in [0,pi).
	 * 
	 * @param x the point where we want to compute the approximation. Note: it is
	 *          supposed to be in [0,pi).
	 * @param n the order of the approximation
	 * @return the value of the approximation
	 */
	public static double macLaurinCosineInZeroPi(double x, int n) {
		double macLaurinApproximation = 0.0; // initialization of the sum
		int factorial = 1;// you can get negative numbers due to an overflow for large m
		for (int i = 0; i < (n + 1); i++) {
			// Maclaurin formula
			macLaurinApproximation += Math.pow(-1, i) * Math.pow(x, 2 * i) / factorial;
			// System.out.println("Power = " + Math.pow(x, 2 * i));
			// System.out.println("Factorial = " + factorial);
			factorial *= (2 * i + 1);
			factorial *= (2 * i + 2);
		}
		return macLaurinApproximation;
	}

	/**
	 * It computes the Maclaurin series of the cosine in a point x.
	 * 
	 * @param x the point where we want to compute the approximation
	 * @param n the order of the approximation
	 * @return the value of the approximation
	 */
	public static double macLaurinCosineSeries(double d, int n) {

		double reduction = d % (Math.PI);// the reminder of the ratio
		double ratio = d / Math.PI;
		int intReduction = (int) ratio; // ratio downcasted to integer
		// or:
		// int intReduction = (int) Math.floor(ratio);
		if ((intReduction % 2) == 0) { // in this case, the cosine is positive
			return macLaurinCosineInZeroPi(reduction, n);
		} // otherwise it is negative
			// note: we don't need else because return already exit the method
		return macLaurinCosineInZeroPi(reduction, n) * (-1);
	}

	public static void main(String[] args) {
		double valueInside = 1;
		double valueOutside = 7;
		int order = 10;

		double cosWithApproximationValueInside = macLaurinCosineInZeroPi(valueInside, order);
		double cosWithJavaValueInside = Math.cos(valueInside);

		System.out.println("The machine value of cosine at " + valueInside + " is " + cosWithJavaValueInside
				+ "; that of the McLaurin series of order " + order + " is " + cosWithApproximationValueInside);

		System.out.println("The absolute value of the difference is "
				+ Math.abs(cosWithApproximationValueInside - cosWithJavaValueInside));

		System.out.println();

		double cosWithApproximationValueOutside = macLaurinCosineInZeroPi(valueOutside, order);
		double cosWithJavaValueOutside = Math.cos(valueOutside);

		System.out.println("The machine value of cosine at " + valueOutside + " is " + cosWithJavaValueOutside
				+ "; that of the McLaurin series of order " + order + " is " + cosWithApproximationValueOutside);

		System.out.println("The absolute value of the difference is "
				+ Math.abs(cosWithApproximationValueOutside - cosWithJavaValueOutside));

		System.out.println();

		double cosWithApproximationWithAdjustment = macLaurinCosineSeries(valueOutside, order);

		System.out.println("The machine value of cosine at " + valueOutside + " is " + cosWithJavaValueOutside
				+ "; that of the McLaurin series of order " + order
				+ " applying the reduction to the natural phase domain is " + cosWithApproximationWithAdjustment);

		System.out.println("The absolute value of the difference is "
				+ Math.abs(cosWithApproximationWithAdjustment - cosWithJavaValueOutside));

	}

}
