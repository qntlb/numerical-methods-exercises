package com.andreamazzon.exercise11.hedging;


import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class gives an example of delta hedging and hedging errors, possibly using three sensitivity
 * computation methods for the computation of the delta. Both the model of the underlying and the
 * model used to hedge are Black-Scholes models.
 *
 * @author Andrea Mazzon
 *
 */
public class DeltaHedgeTest {

	static NumberFormat formatDec4 = new DecimalFormat("0.0000");
	public static void main(String[] args) throws CalculationException {


		final int numberOfTimeSteps = 5000;
		final int numberOfSimulations = 1;
		final double initialPrice = 100.0;
		final double timeHorizon = 1.0;
		final double volatility = 0.3;
		final double volatilityForHedge = 0.3;

		final double riskFreeRate = 0.3;

		// Bs simulation
		final TimeDiscretization times = new TimeDiscretizationFromArray(0.0, numberOfTimeSteps,
				timeHorizon / numberOfTimeSteps);

		/*
		 * Model for hedging: a priori could have different parameters from that of the model for
		 * the underlying
		 */
		final MonteCarloBlackScholesModel blackScholesHedging = new MonteCarloBlackScholesModel(
				times, numberOfSimulations, initialPrice, riskFreeRate, volatility);

		final double strike = 100;
		final double maturity = timeHorizon;

		final DeltaHedge deltaHedge = new DeltaHedge(blackScholesHedging,
				volatilityForHedge, riskFreeRate, strike, maturity);

		final double[] stockPrices = deltaHedge.getPathOfUnderlying();
		final double[] optionPrices = deltaHedge.getOptionPrices();
		final double[] deltas = deltaHedge.getDeltas();
		final double[] bankAccount = deltaHedge.getBankAccount();
		final double[] portfolioValue = deltaHedge.getPortfolioValue();
		final double[] hedgeError = deltaHedge.getHedgeError();

		System.out.print("Stock prices   ");
		System.out.print("Option prices     ");
		System.out.print("Deltas     ");
		System.out.print("Bank account    ");
		System.out.print("Portfolio value   ");
		System.out.println("Hedging error   ");

		for (int timeIndex = 0; timeIndex < times.getNumberOfTimes(); timeIndex ++ ) {
			System.out.print(formatDec4.format(stockPrices[timeIndex])+ "      ");
			System.out.print(formatDec4.format(optionPrices[timeIndex])+ "            ");
			System.out.print(formatDec4.format(deltas[timeIndex])+ "     ");
			System.out.print(formatDec4.format(bankAccount[timeIndex])+ "         ");
			System.out.print(formatDec4.format(portfolioValue[timeIndex])+ "         ");
			System.out.println(formatDec4.format(hedgeError[timeIndex]));
		}

		System.out.println("Average absolute error: " + UsefulMethodsMatricesVectors.getAverage(
				UsefulMethodsMatricesVectors.absVector(hedgeError)));
	}
}
