package com.andreamazzon.exercise10.products;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import net.finmath.exception.CalculationException;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This test class compares the analytical value of an exchange option with the one computed
 * by Monte Carlo, and tests if they are close enough. It also tests if, computing the Monte
 * Carlo price with different (random) seeds, it happens to be "close" to the analytical price
 * a given percentage of times.
 *
 * @author Andrea Mazzon
 *
 */
class ExchangeOptionTest {

	/*
	 * parameters for the model: remember that we want to construct a two-dimensional
	 * process
	 */
	final double[] initialPrices = {100.0,100.0};

	final double[] volatilities = {0.25, 0.3};

	final double riskFreeRate = 0;

	final double correlation = 0.4;
	/*
	 * correlation matrix: to be given to MonteCarloMultiAssetBlackScholesModel to construct
	 * the simulation of two possibly correlated geometric Brownian motions
	 */
	final double[][] correlationMatrix = {{1.0, correlation},{correlation,1.0}};


	//parameters for simulation and time discretization
	final int numberOfSimulations = 5000;//number of paths

	final double initialTime = 0;
	final double maturity = 1.0;
	final int numberOfTimeSteps = 100;
	final double timeStep = maturity / numberOfTimeSteps;
	TimeDiscretization times = new TimeDiscretizationFromArray(initialTime,
			numberOfTimeSteps, timeStep);

	//tolerance for the test
	final double tolerance = 0.05;

	//allowed failure percentage for the second test
	final double allowedFailurePercentage = 0.1;


	/*
	 * This method computes the analytical value of an exchange option with maturity
	 * given by the field of the class, for a two-dimensional Brownian motion with parameters
	 * also given above, using the analytic formula you can find in the exercise sheet.
	 * Give the implementation of this method.
	 */

	private double computeAnalyticalValue() {
		return 0;
	}

	/**
	 * Tests if the value of the exchange option computed by Monte Carlo for a given seed
	 * is close up to the given tolerance to the analytical one.
	 * @throws CalculationException
	 */
	@Test
	public void testExchange() throws CalculationException {
		final int seed = 1897;

		/*
		 * Here we want to construct an object of type AssetModelMonteCarloSimulationModel, that specifically
		 * simulates a two-dimensional, possibly correlated geometric Brownian motion. This can be done using
		 * one of the two constructors of
		 * net.finmath.montecarlo.assetderivativevaluation.MonteCarloMultiAssetBlackScholesModel.
		 * Have a look at them, decide which one you want to use and call it in order to create your object,
		 * after having constructed the objects you have to give to that constructor.
		 * Call the object you obtain in this way simulationTwoDimGeometricBrownian.
		 */


		/*
		 * After having done this, create an object of ExchangeOption with maturity given above,
		 * and call its getValue(final MonteCarloSimulationModel model) method, passing it
		 * simulationTwoDimGeometricBrownian. This method is an overloading of
		 * getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
		 * inherited from AbstractMonteCarloProduct. It returns a double which is the price of
		 * the option.
		 */
		//we call the overloaded constructor
		final double monteCarloPrice = 0;
		final double analyticalPrice = computeAnalyticalValue();
		final double error = Math.abs(monteCarloPrice - analyticalPrice)/analyticalPrice;
		System.out.println("Simulated price of the exchange option using multi asset MC " +
				monteCarloPrice );
		System.out.println("Analytical price " + analyticalPrice );
		System.out.println("Relative error: " + error );
		System.out.println();
		Assert.assertTrue(error<tolerance);
		//or Assert.assertEquals(0,error,tolerance);
	}
	/**
	 * Repeats the computation of the Monte Carlo price of an exchange option
	 * for different random seeds, and it checks if this price is close to the
	 * analytical one up to a given tolerance, a given percentage of times.
	 * @throws CalculationException
	 */
	@Test
	public void testExchangeWithRandomSeeds() throws CalculationException {

		final int numberOfRepetitions = 500;//number of Monte Carlo computations
		int count = 0;
		//we want to get a random integer: we use the Random class of Java
		final Random rng = new Random();

		for (int i=0; i< numberOfRepetitions; i++) {
			final int randomSeed = rng.nextInt();

			/*
			 * get the price of your exchange option as above, now constructing your
			 * MonteCarloMultiAssetBlackScholesModel with randomSeed.
			 */

			final double monteCarloPrice = 0;
			final double analyticalPrice = computeAnalyticalValue();
			final double error = Math.abs(monteCarloPrice - analyticalPrice)/analyticalPrice;
			if (error > tolerance) {
				count ++; //counter updated if the relative error exceeds the tolerance
			}

		}
		final double ratioFailure = ((double)count)/numberOfRepetitions;
		final double percentageSuccess = (1-ratioFailure)*100;
		System.out.println("The percentage of times when the relative error is smaller than "
				+ tolerance + " is " + percentageSuccess);
		System.out.println();
		Assert.assertTrue(ratioFailure < allowedFailurePercentage);
	}
}

