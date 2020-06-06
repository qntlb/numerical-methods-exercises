package com.andreamazzon.exercise7.bivariatenormal;

import java.text.DecimalFormat;

import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

public class BivariateNormalTesting {

	/**
	 * It tests the precision and the efficiency of a selected method, by means of a
	 * switch statement based on an enum type containing the names of the methods.
	 *
	 * @param normalTestSampler,    object of type NormalRandomVariable. It calls
	 *                              the generation methods
	 * @param method,               enum type whose value is the name of one of the
	 *                              four generation methods.
	 * @param numberOfDrawings,     the number of the generated pairs
	 * @param numberOfComputations, the number of Monte-Carlo approximations
	 */
	public static void testTheMethods(NormalRandomVariable normalTestSampler, GenerationMethods method,
			int numberOfDrawings, int numberOfComputations) {

		// format in order to print a number with 5 decimal digits
		DecimalFormat formatterValue = new DecimalFormat(" ##0.00000;");

		/*
		 * mean of the two normal random variables Z_1,Z_2 (they are independent and
		 * have same distribution)
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
					/*
					 * TO DO: SAMPLE A PAIR OF INDEPENDENT NORMAL RANDOM VARIABLES BY USING THE
					 * METHOD GenerateBivariateNormal(), AND CHECK IF BOTH THE VALUES ARE <= MU. IF
					 * IT IS THE CASE, INCREASE numberOfTimesBothSmallerThanMu BY 1.
					 */
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
			/*
			 * for every Monte-Carlo approximation, we compute the percentage error and the
			 * time needed to do the computation. Then we compute the average.
			 */
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * you compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					/*
					 * TO DO: SAMPLE A PAIR OF INDEPENDENT NORMAL RANDOM VARIABLES BY USING THE
					 * METHOD GenerateBivariateNormalAR(), AND CHECK IF BOTH THE VALUES ARE <= MU.
					 * IF IT IS THE CASE, INCREASE numberOfTimesBothSmallerThanMu BY 1.
					 */
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
			/*
			 * for every Monte-Carlo approximation, we compute the percentage error and the
			 * time needed to do the computation. Then we compute the average.
			 */
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * you compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					/*
					 * TO DO: SAMPLE A PAIR OF INDEPENDENT NORMAL RANDOM VARIABLES BY USING THE
					 * METHOD generateBoxMuller(), AND CHECK IF BOTH THE VALUES ARE <= MU. IF IT IS
					 * THE CASE, INCREASE numberOfTimesBothSmallerThanMu BY 1.
					 */
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
			/*
			 * for every Monte-Carlo approximation, we compute the percentage error and the
			 * time needed to do the computation. Then we compute the average.
			 */
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * you compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					/*
					 * TO DO: SAMPLE A PAIR OF INDEPENDENT NORMAL RANDOM VARIABLES BY USING THE
					 * METHOD generateARBoxMuller(), AND CHECK IF BOTH THE VALUES ARE <= MU. IF IT
					 * IS THE CASE, INCREASE numberOfTimesBothSmallerThanMu BY 1.
					 */
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
		 * TO DO: USE A FOR LOOP (POSSIBLY WITH THE FOREACH SYNTAX AND THE values()
		 * METHOD OF enum TYPES) IN ORDER TO CALL testTheMethods FOR EVERY GENERATION
		 * METHOD.
		 */
	}
}
