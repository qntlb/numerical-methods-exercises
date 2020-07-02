package com.andreamazzon.exercise10.products;

import java.util.function.DoubleUnaryOperator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This test class compares the anaytical value of a call option with the one computed
 * by Monte Carlo, and tests if they are close enough
 * @author Andrea Mazzon
 *
 */
class GeneralOptionTest {

        @Test
        void testCall() throws CalculationException {

                //model parameters

                final double initialPrice = 100.0;
                final double volatility = 0.25;
                final double riskFreeRate = 0;

                //simulation and time discretization parameters
                final int numberOfSimulations = 100000;//number of paths

                final double initialTime = 0;
                final double maturity = 1.0;
                final int numberOfTimeSteps = 1000;
                final double timeStep = maturity / numberOfTimeSteps;
                final TimeDiscretization times = new TimeDiscretizationFromArray(initialTime,
                                numberOfTimeSteps, timeStep);

                //option parameters
                final double strike = 100.0;
                
                //parameter for the test
                final double tolerance = 1e-15;
                
                /*
                 * DoubleUnaryOperator object specifying the payoff function of an european call.
                 * Change its value to define the payoff of european call.
                 */
                final DoubleUnaryOperator payoffFunction = (x) -> Math.max(x-strike, 0);

                final double analyticValue = AnalyticFormulas.blackScholesOptionValue(
                                initialPrice, riskFreeRate, volatility, maturity, strike);

                //model and process linked together!
                final AssetModelMonteCarloSimulationModel bsModel = new MonteCarloBlackScholesModel(
                                times, numberOfSimulations, initialPrice, riskFreeRate, volatility);

                final GeneralOption ourOption = new GeneralOption(maturity, payoffFunction);
                final EuropeanOption europeanOption = new EuropeanOption(maturity, strike);

                final double valueWithGeneralOption = ourOption.getValue(bsModel);
                final double valueWithSpecificEuropeanCall = europeanOption.getValue(bsModel);

                System.out.println("Value with general option: " + valueWithGeneralOption
                                + "\n" + "value with specific european call class: "
                                + valueWithSpecificEuropeanCall
                                + "\n" + "analytical value: " + analyticValue);

                Assert.assertEquals(valueWithGeneralOption, valueWithSpecificEuropeanCall,
                                tolerance);
        }
}
