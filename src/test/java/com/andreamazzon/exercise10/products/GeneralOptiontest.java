package com.andreamazzon.exercise10.products;

import java.util.function.DoubleUnaryOperator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
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
		/*
		 * DoubleUnaryOperator object specifying the payoff function of an european call.
		 * Change its value to define the payoff of european call.
		 */
		final DoubleUnaryOperator payoffFunction = (x) -> x;

		//parameter for the tests
		final double tolerance = 1e-15;

		final double analyticValue = AnalyticFormulas.blackScholesOptionValue(
				initialPrice, riskFreeRate, volatility, maturity, strike);

		/*
		 * create the underlying to be passed in the constructor of your GeneralOption
		 * and to the one of
		 * net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption.
		 * Call then the two constructors and get the price with
		 * getValue(final MonteCarloSimulationModel model)
		 * We want then to see if the values coincide.
		 */
		//model and process glued together!

		final double valueWithGeneralOption = 0;
		final double valueWithSpecificEuropeanCall = 0;

		System.out.println("Value with general option: " + valueWithGeneralOption
				+ "\n" + "value with specific european call class: "
				+ valueWithSpecificEuropeanCall
				+ "\n" + "analytical value: " + analyticValue);

		Assert.assertEquals(valueWithGeneralOption, valueWithSpecificEuropeanCall,
				tolerance);
	}
}
