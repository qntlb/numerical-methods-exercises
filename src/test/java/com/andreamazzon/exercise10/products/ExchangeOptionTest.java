package com.andreamazzon.exercise10.products;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloMultiAssetBlackScholesModel;
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

        double correlation = -1;
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

        //tolerance for the test, in percentage
        final double percentageTolerance = 5;

        //allowed failure percentage for the second test
        final double allowedFailurePercentage = 5;


        /*
         * This method computes the analytical value of an exchange option with maturity
         * given by the field of the class, for a two-dimensional Brownian motion with parameters
         * also given above, using the analytic formula you can find in the exercise sheet.
         * Give the implementation of this method.
         */

        private double computeAnalyticalValue() {
                final double sigma = Math.sqrt(volatilities[0]*volatilities[0] -
                                2*volatilities[0]*volatilities[1]*correlation +
                                volatilities[1]*volatilities[1]);
                return AnalyticFormulas.blackScholesOptionValue(
                                initialPrices[0], riskFreeRate, sigma, maturity, initialPrices[1]);
        }
        
        /**
         * This method tests the dependence of the price of the payoff with respect to the correlation between the
         * two assets. We can see that the price is decraesing with respect to the correlation
         * 
         * @throws CalculationException
         */
        @Test 
        public void testCorrelation() throws CalculationException{
        	final BrownianMotion brownian = new BrownianMotionFromMersenneRandomNumbers(times, 2,
                    numberOfSimulations, 1897);//(B^1,B^2)
        	System.out.print("Correlation    ");
        	System.out.println("Price");

        	for (int i = 0; i<= 20; i++){
        		correlation = (i-10) * 0.1;//the correlation goes from -1 to 1
        		final double[][] correlationMatrix = {{1.0, correlation},{correlation,1.0}};
 
        		final AssetModelMonteCarloSimulationModel simulationTwoDimGeometricBrownian =
                        new MonteCarloMultiAssetBlackScholesModel(
                                        brownian, initialPrices, riskFreeRate, volatilities, correlationMatrix);
        		final ExchangeOption exchangeOption=new ExchangeOption(maturity);
        		final double monteCarloPrice = exchangeOption.getValue(simulationTwoDimGeometricBrownian);
        		System.out.print(correlation + "   ");
        		System.out.println(monteCarloPrice);
        	}
        }
        
        /**
         * Tests if the value of the exchange option computed by Monte Carlo for a given seed
         * is close up to the given tolerance to the analytical one.
         * @throws CalculationException
         */
        @Test
        public void testExchange() throws CalculationException {
                final int seed = 1897;
                //W_1 = B_1
                //W_2 = rho B_1 + sqrt(1-rho^2)B^2
                //here you simulate B^1, B^2 independent 
                //Two-dimensional Brownian motion
                final BrownianMotion brownian = new BrownianMotionFromMersenneRandomNumbers(times, 2,
                                numberOfSimulations, seed);//(B^1,B^2)
                /*
                 * finmath class, we use it to get the simulation of a two dimensional Black-Scholes
                 * model: two geometric Brownian motions, with possibly dependent stochastic drivers
                 * (correlation specified by the correlation matrix). Note that the class implements
                 * AssetModelMonteCarloSimulationModel.
                 */
                final AssetModelMonteCarloSimulationModel simulationTwoDimGeometricBrownian =
                                new MonteCarloMultiAssetBlackScholesModel(
                                                brownian, initialPrices, riskFreeRate, volatilities, correlationMatrix);
                /*
                 * We now create an object of ExchangeOption with maturity given above,
                 * and call its getValue(final MonteCarloSimulationModel model) method, passing it
                 * simulationTwoDimGeometricBrownian. This method is an overloading of
                 * getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
                 * inherited from AbstractMonteCarloProduct. It returns a double which is the price of
                 * the option.
                 */
                //we call the overloaded constructor
                final ExchangeOption exchangeOption=new ExchangeOption(maturity);
                final double monteCarloPrice = exchangeOption.getValue(simulationTwoDimGeometricBrownian);
                final double analyticalPrice = computeAnalyticalValue();
                final double error = Math.abs(monteCarloPrice - analyticalPrice)/analyticalPrice*100;
                System.out.println("Simulated price of the exchange option using multi asset MC " +
                                monteCarloPrice );
                System.out.println("Analytical price " + analyticalPrice );
                System.out.println("Percentage error: " + error );
                System.out.println();
                Assert.assertTrue(error<percentageTolerance);
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
                        final BrownianMotion brownian = new BrownianMotionFromMersenneRandomNumbers(times, 2, // Bi-Dimensional Brownian Motion!
                                        numberOfSimulations, randomSeed/*the seed changes at every iterations*/);
                        final AssetModelMonteCarloSimulationModel simulationTwoDimGeometricBrownian = new
                                        MonteCarloMultiAssetBlackScholesModel(
                                                        brownian, initialPrices, riskFreeRate, volatilities,
                                                        correlationMatrix);

                        final ExchangeOption exchangeOption=new ExchangeOption(maturity);

                        final double monteCarloPrice = exchangeOption.getValue(simulationTwoDimGeometricBrownian);
                        final double analyticalPrice = computeAnalyticalValue();
                        final double error = Math.abs(monteCarloPrice - analyticalPrice)/analyticalPrice*100;
                        if (error > percentageTolerance) {
                                count ++; //counter updated if the relative error exceeds the tolerance
                        }

                }
                final double ratioFailure = ((double)count)/numberOfRepetitions*100;
                final double percentageSuccess = 100-ratioFailure;
                System.out.println("The percentage of times when the percentage error is smaller than "
                                + percentageTolerance + " is " + percentageSuccess);
                System.out.println();
                Assert.assertTrue(ratioFailure < allowedFailurePercentage);
        }
        
        
       
}

