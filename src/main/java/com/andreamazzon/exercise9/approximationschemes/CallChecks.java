package com.andreamazzon.exercise9.approximationschemes;

import java.util.Random;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class has a main method which computes the average percentage error in the computation of the
 * price of a call option under the Black-Scholes model, for three different methods used to simulate the
 * underlying process: Euler scheme, "log Euler" scheme (i.e., simulating the logarithm of the underlying by an
 * Euler scheme) and Milstein scheme.
 *
 * @author Andrea Mazzon
 *
 */
public class CallChecks {

	public static void main(String[] args) throws CalculationException {

		final int numberOfSimulations = 10000;//number of simulated paths

		final double initialPrice = 100.0;
		final double riskFreeRate = 0; //this will be the drift: we simulate under the risk neutral probability measure
		final double volatility = 0.25;

		//time discretization
		final double initialTime = 0;
		final double timeHorizon = 1.0;//it is also the maturity of the option
		final int numberOfTimeSteps = 100;
		final double delta = timeHorizon / numberOfTimeSteps;
		final TimeDiscretization times = new TimeDiscretizationFromArray(initialTime,
				numberOfTimeSteps, delta);

		final double strike = 100.0;

		//note: analytic value given in the Finmath library
		final double analyticCallValue = AnalyticFormulas.blackScholesOptionValue(initialPrice,
				riskFreeRate, volatility, timeHorizon, strike);

		final int numberOfTests = 500;

		double errorEulerCalls;
		double errorLogEulerCalls;
		double errorMilsteinCalls;

		double averageErrorEuler = 0;
		double averageErrorLogEuler = 0;
		double averageErrorMilstein = 0;

		final Random randomGenerator = new Random();

		for (int i = 0; i < numberOfTests; i++) {

			//randomized seed; for every test we will have (very probably) a different seed
			final int seed = randomGenerator.nextInt();

			//the three simulations:
			final AbstractSimulation euler = new EulerScheme(numberOfSimulations, volatility,
					riskFreeRate, initialPrice, seed, times);

			final AbstractSimulation logEuler = new LogEulerScheme(numberOfSimulations, volatility,
					riskFreeRate, initialPrice, seed, times);

			final AbstractSimulation milstein = new MilsteinScheme(numberOfSimulations, volatility,
					riskFreeRate, initialPrice, seed, times);

			//three different objects for every method, with three different underlyings
			final CallOption callOptionEuler = new CallOption(euler);
			final CallOption callOptionLogEuler = new CallOption(logEuler);
			final CallOption callOptionMilstein = new CallOption(milstein);

			//the three values of the error computing the price of the (discounted!) call option
			errorEulerCalls = Math.abs(callOptionEuler.priceCall(strike,timeHorizon,riskFreeRate)-analyticCallValue)
					/analyticCallValue*100;
			errorLogEulerCalls = Math.abs(callOptionLogEuler.priceCall(strike,timeHorizon,riskFreeRate)-analyticCallValue)
					/analyticCallValue*100;
			errorMilsteinCalls = Math.abs(callOptionMilstein.priceCall(strike,timeHorizon,riskFreeRate)-analyticCallValue)
					/analyticCallValue*100;

			//we update the average
			averageErrorEuler = (averageErrorEuler * i + errorEulerCalls) / (i+1);
			averageErrorLogEuler = (averageErrorLogEuler * i + errorLogEulerCalls) / (i+1);
			averageErrorMilstein = (averageErrorMilstein * i + errorMilsteinCalls) / (i+1);

		}

		System.out.println("Analytical price of the call: " + analyticCallValue);
		System.out.println("Average error for Euler scheme: " + averageErrorEuler);
		System.out.println("Average error for log Euler scheme: " + averageErrorLogEuler);
		System.out.println("Average error for Milstein scheme: " + averageErrorMilstein);
	}
}
