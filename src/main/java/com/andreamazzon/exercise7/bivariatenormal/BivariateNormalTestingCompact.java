package com.andreamazzon.exercise7.bivariatenormal;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;

import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

/**
 * In this class we do the same tests as in BivariateNormalTesting, but in a
 * much more compact way. We use a Callable interface in order to avoid to write
 * the same code for all the different GenerationMethods, a part from the name
 * of the method to be executed.
 *
 * @author Andrea Mazzon
 *
 */
public class BivariateNormalTestingCompact {

	/*
	 * Note: all the variables that used to be defined and initialized directly in
	 * testMethod in BivariateNormalTesting are now fields of the class. They have
	 * to be static, as they will be called by static methods.
	 */

	// format in order to print a number with 5 decimal digits
	static DecimalFormat formatterValue = new DecimalFormat(" ##0.00000;");

	// These fields are initialized here.
	static double exactResult = 0.25;// because Z_1 and Z_2 are independent
	static double averageElapsedTime = 0;
	static double averageError = 0;

	/*
	 * expected value of the two normal random variables Z_1,Z_2 (they are
	 * independent and have same distribution). It will be initialed in the method,
	 * once we know our NormalRandomVariable object.
	 */
	static double mu;

	// they will be initialized in the method
	static int numberOfComputations;
	static int numberOfDrawings;

	/*
	 * This method computes and returns the average percentage error and the average
	 * time needed to compute the approximation, only basing on a Callable<double[]>
	 * object that we call function. You can see it as a placeholder for the
	 * specific method that we will call depending on the chosen generation method.
	 * For now, we only know that it will return an array of doubles (as we specify
	 * between angular brackets).
	 */
	private static double[] testMethod(Callable<double[]> function) throws Exception {
		/*
		 * for every Monte-Carlo approximation, we compute the percentage error and the
		 * time needed to do the computation. Then we compute the average.
		 */
		for (int i = 0; i < numberOfComputations; i++) {
			/*
			 * We compute for how many generated pairs both the values are smaller than mu
			 */
			double numberOfTimesBothSmallerThanMu = 0.0;
			final long lStartTime = System.currentTimeMillis();// time when the computations starts
			for (int j = 0; j < numberOfDrawings; j++) {
				/*
				 * NOTE!!! here our Callable<double[]> comes into play: generatedPair is the
				 * array of doubles returned by function when called (we know that it returns an
				 * array of doubles)
				 */
				final double[] generatedPair = function.call();// note the syntax
				if (generatedPair[0] < mu && generatedPair[1] < mu) {
					numberOfTimesBothSmallerThanMu++;
				}
			}
			/*
			 * number of generated pairs for which both the values are smaller then the mean
			 * divided by the number of simulations: you expect the result to be close to
			 * 0.25
			 */
			final double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
			final long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the
			// method.
			final double elapsedTime = lEndTime - lStartTime;
			averageElapsedTime = (averageElapsedTime * i + elapsedTime) / (i + 1);
			final double error = Math.abs(frequence - exactResult) / exactResult * 100;
			averageError = (averageError * i + error) / (i + 1);
		}
		return new double[] { averageError, averageElapsedTime };
	}

	/**
	 * It tests the precision and the efficiency of a selected method to generate a
	 * pair of independent normal random variables with expectation mu and standard
	 * deviation sigma. The method (inversion sampling, acceptance rejection,
	 * Box-Muller, acceptance rejection Box Muller) is selected by means of a switch
	 * statement based on an enum type containing the names of the methods. The test
	 * is to compute the Monte-Carlo approximation of P(X_1<mu, X_2 <mu) where X_1,
	 * X_2 are independent, normal random variables with expectation mu. For every
	 * method we compute and print the average percentage error with respect to the
	 * exact probability 0.25 and the time needed in order to generate the drawings
	 * of (X_1,X_2) and do the computations.
	 *
	 * @param normalTestSampler,    object of type NormalRandomVariable. It calls
	 *                              the generation methods
	 * @param method,               enum type whose value is the name of one of the
	 *                              four generation methods.
	 * @param numberOfDrawings,     the number of the generated pairs
	 * @param numberOfComputations, the number of Monte-Carlo approximations
	 * @throws Exception
	 */
	public static void testMethod(NormalRandomVariable normalTestSampler, GenerationMethods method,
			int numberOfDrawings, int numberOfComputations) throws Exception {

		// these are fields of the class now!
		BivariateNormalTestingCompact.numberOfDrawings = numberOfDrawings;
		BivariateNormalTestingCompact.numberOfComputations = numberOfComputations;

		/*
		 * expected value of the two normal random variables Z_1,Z_2 (they are
		 * independent and have same distribution)
		 */
		mu = normalTestSampler.getAnalyticMean();// field of the class now!

		double[] values = null;// we have to initialize it before the switch
		Callable<double[]> functionToEvaluate;
		/*
		 * Here we have a switch based on the name of the four methods, see the enum
		 * type GenerationMethods
		 */
		switch (method) {// name of the method
		case BIVARIATENORMAL:
			System.out.println("Bivariate Normal");
			// we now say what must be returned by functionToEvaluate in this case
			functionToEvaluate = () -> normalTestSampler.generateBivariateNormal();
			values = testMethod(functionToEvaluate);
			break;

		case ACCEPTANCEREJECTION:
			System.out.println("Acceptance rejection");
			// we now say what must be returned by functionToEvaluate in this case
			functionToEvaluate = () -> normalTestSampler.generateBivariateNormalAR();
			values = testMethod(functionToEvaluate);
			break;

		case BOXMULLER:
			System.out.println("Box Muller");
			// we now say what must be returned by functionToEvaluate in this case
			functionToEvaluate = () -> normalTestSampler.generateBoxMuller();
			values = testMethod(functionToEvaluate);
			break;

		case ARBOXMULLER:
			System.out.println("Acceptance rejection Box Muller");
			// we now say what must be returned by functionToEvaluate in this case
			functionToEvaluate = () -> normalTestSampler.generateARBoxMuller();
			values = testMethod(functionToEvaluate);
			break;
			/*
			 * no need for default case here: we don't have to return something, so the
			 * compiler does not complain: it does if you have to return something, because
			 * in that case you have to return in any case (so also if no one of the cases
			 * of the switch statement is fulfilled).
			 */
		}

		final double averageError = values[0];
		final double averageElapsedTime = values[1];

		System.out.println("Average elapsed time: " + formatterValue.format(averageElapsedTime));
		System.out.println("Average error: " + formatterValue.format(averageError));
		System.out.println();
	}

	public static void main(String[] args) throws Exception {

		final double mu = 2;
		final double sigma = 1.0;
		final int numberOfDrawings = 10000;
		final int numberOfComputations = 10000;
		final NormalRandomVariable normalTestSampler = new NormalRandomVariable(mu, sigma);
		/*
		 * we test the method for every choice: the vector of all the names of the
		 * method is given by the static values() method of GenerationMethods: this is
		 * implemented in Java
		 */
		for (final GenerationMethods modelSelector : GenerationMethods.values()) {// foreach syntax
			testMethod(normalTestSampler, modelSelector, numberOfDrawings, numberOfComputations);
		}
	}
}