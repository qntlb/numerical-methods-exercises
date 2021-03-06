package com.andreamazzon.exercise7.bivariatenormal;

import java.text.DecimalFormat;

import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

public class BivariateNormalTesting {

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
	 */
	public static void testMethod(NormalRandomVariable normalTestSampler, GenerationMethods method,
			int numberOfDrawings, int numberOfComputations) {

		// format in order to print a number with 5 decimal digits
		DecimalFormat formatterValue = new DecimalFormat(" ##0.00000;");

		/*
		 * expected value of the two normal random variables Z_1,Z_2 (they are
		 * independent and have same distribution)
		 */
		double mu = normalTestSampler.getAnalyticMean();

		// because Z_1 and Z_2 are independent
		double exactResult = 0.25;
		double averageElapsedTime = 0;
		double averageError = 0;

		/*
		 * Here we have a switch based on the name of the four methods, see the enum
		 * type GenerationMethods
		 */
		switch (method) {// name of the method
		case BIVARIATENORMAL:
			System.out.println("Bivariate Normal");
			/*
			 * for every Monte-Carlo approximation, we compute the percentage error and the
			 * time needed to do the computation. Then we compute the average.
			 */
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * We compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					double[] generatedPair = normalTestSampler.generateBivariateNormal();
					if (generatedPair[0] < mu && generatedPair[1] < mu) {
						numberOfTimesBothSmallerThanMu++;
					}
				}
				/*
				 * number of generated pairs for which both the values are smaller then the mean
				 * divided by the number of simulations: you expect the result to be close to
				 * 0.25
				 */
				double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
				long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the method.
				double elapsedTime = lEndTime - lStartTime;
				averageElapsedTime = (averageElapsedTime * i + elapsedTime) / (i + 1);
				double error = Math.abs(frequence - exactResult) / exactResult * 100;
				averageError = (averageError * i + error) / (i + 1);
			}
			break;
		case ACCEPTANCEREJECTION:
			System.out.println("Acceptance rejection");
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * you compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					double[] generatedPair = normalTestSampler.generateBivariateNormalAR();
					if (generatedPair[0] < mu && generatedPair[1] < mu) {
						numberOfTimesBothSmallerThanMu++;
					}
				}
				/*
				 * number of generated pairs for which both the values are smaller then the mean
				 * divided by the number of simulations: you expect the result to be close to
				 * 0.25
				 */
				double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
				long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the method.
				double elapsedTime = lEndTime - lStartTime;
				averageElapsedTime = (averageElapsedTime * i + elapsedTime) / (i + 1);
				double error = Math.abs(frequence - exactResult) / exactResult * 100;
				averageError = (averageError * i + error) / (i + 1);
			}
			break;
		case BOXMULLER:
			System.out.println("Box Muller");
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * you compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					double[] generatedPair = normalTestSampler.generateBoxMuller();
					if (generatedPair[0] < mu && generatedPair[1] < mu) {
						numberOfTimesBothSmallerThanMu++;
					}
				}
				/*
				 * number of generated pairs for which both the values are smaller then the mean
				 * divided by the number of simulations: you expect the result to be close to
				 * 0.25
				 */
				double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
				long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the method.
				double elapsedTime = lEndTime - lStartTime;
				averageElapsedTime = (averageElapsedTime * i + elapsedTime) / (i + 1);
				double error = Math.abs(frequence - exactResult) / exactResult * 100;
				averageError = (averageError * i + error) / (i + 1);
			}
			break;
		case ARBOXMULLER:
			System.out.println("Acceptance rejection Box Muller");
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * you compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					double[] generatedPair = normalTestSampler.generateARBoxMuller();
					if (generatedPair[0] < mu && generatedPair[1] < mu) {
						numberOfTimesBothSmallerThanMu++;
					}
				}
				/*
				 * number of generated pairs for which both the values are smaller then the mean
				 * divided by the number of simulations: you expect the result to be close to
				 * 0.25
				 */
				double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
				long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the method.
				double elapsedTime = lEndTime - lStartTime;
				averageElapsedTime = (averageElapsedTime * i + elapsedTime) / (i + 1);
				double error = Math.abs(frequence - exactResult) / exactResult * 100;
				averageError = (averageError * i + error) / (i + 1);
			}
			break;
			/*
			 * no need for default case here: we don't have to return something, so the
			 * compiler does not complain: it does if you have to return something, because
			 * in that case you have to return in any case (so also if no one of the cases
			 * of the switch statement is fulfilled).
			 */
		}
		System.out.println("Average elapsed time: " + formatterValue.format(averageElapsedTime));
		System.out.println("Average error: " + formatterValue.format(averageError));
		System.out.println();
	}

	public static void main(String[] args) {

		double mu = 2;
		double sigma = 1.0;
		int numberOfDrawings = 10000;
		int numberOfComputations = 10000;
		NormalRandomVariable normalTestSampler = new NormalRandomVariable(mu, sigma);
		/*
		 * we test the method for every choice: the vector of all the names of the
		 * method is given by the static values() method of GenerationMethods: this is
		 * implemented in Java
		 */
		for (GenerationMethods modelSelector : GenerationMethods.values()) {// foreach syntax
			testMethod(normalTestSampler, modelSelector, numberOfDrawings, numberOfComputations);
		}
	}

}