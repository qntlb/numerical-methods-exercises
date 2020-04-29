package com.andreamazzon.exercise1;

/**
 * This class deals with operations concerning double and float numbers: it can
 * be seen that one has to pay attention when checking if two numbers of this
 * type are equal.
 * 
 * @author Andrea Mazzon
 *
 */
public class FloatAndDouble {

	/**
	 * Computes the biggest epsilon of the form epsilon = 2^(-n) such that
	 * |x-x0|<2^(-n).
	 * 
	 * @param x  double
	 * @param x0 double
	 * @return an object of the container class EpsilonAndExponent, where we have
	 *         set the values of the biggest epsilon of the form epsilon = 2^(-n)
	 *         such that |x-x0|<2^(-n), and the correspondent int n.
	 */
	public static EpsilonAndExponent computeBiggestEpsilon(double x, double x0) {
		double difference = Math.abs(x0 - x);
		double epsilon = 1.0;
		int exponent = 0;
		// computation of epsilon and of the exponent
		while (difference < epsilon) {// we stop as soon as epsilon < |x-x0|
			epsilon /= 2.0; // epsilon=epsilon/2
			exponent++;
		}
		/*
		 * we create this class because epsilon is a double and exponent int, and we
		 * cannot have an array with different types. Two different ways to do this
		 * could be: - upcast exponent to double - create and return an
		 * ArrayList<Object>
		 */
		EpsilonAndExponent epsilonAndExponent = new EpsilonAndExponent();
		// we set the values of the epsilon and exponent field of the class
		epsilonAndExponent.setEpsilon(epsilon);
		epsilonAndExponent.setExponent(exponent);
		return epsilonAndExponent;
	}

	public static void main(String[] args) {
		double x0 = 3.0 * 0.1; // double
		float xFloat = 0.3f; // float: you have to type f, otherwise it complains
		System.out.println("The statement xFloat=x0 is " + (xFloat == x0));
		/*
		 * NOTE: the float x is upcasted to double when the method gets called, but when
		 * the method is executed, it recognises that it is a float: late binding! Note
		 * that the result is different when both the numbers are double! Just as with
		 * inheritance
		 */
		EpsilonAndExponent epsilonAndExponentWithFloat = computeBiggestEpsilon(xFloat, x0);
		double epsilonWithFloat = epsilonAndExponentWithFloat.getEpsilon();
		int exponentWithFloat = epsilonAndExponentWithFloat.getExponent();

		System.out.println("The smallest power n such that |xFloat-x0|<2^(-n) is  " + exponentWithFloat
				+ " , for which 2^(-n) equals " + epsilonWithFloat);

		System.out.println();
		double xDouble = 0.3; // now it's double
		System.out.println("The statement xDouble=x0 is " + (xDouble == x0));

		EpsilonAndExponent epsilonAndExponentWithDouble = computeBiggestEpsilon(xDouble, x0);
		double epsilonWithDouble = epsilonAndExponentWithDouble.getEpsilon();
		int exponentWithDouble = epsilonAndExponentWithDouble.getExponent();

		System.out.println("The smallest power n such that |xDouble-x0|<2^(-n) is  " + exponentWithDouble
				+ " , for which 2^(-n) equals " + epsilonWithDouble);
	}

}
