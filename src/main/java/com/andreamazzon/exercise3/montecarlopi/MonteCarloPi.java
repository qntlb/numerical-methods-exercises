package com.andreamazzon.exercise3.montecarlopi;

import com.andreamazzon.exercise3.montecarlo.MonteCarloExperimentsWithExactResult;

/**
 * This class regards the approximation of Pi by Monte-Carlo. It implements
 * MonteCarloExperimentsWithExactResult, since we can compare the values we get
 * with the value of pi given by Java.
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloPi extends MonteCarloExperimentsWithExactResult {

	public MonteCarloPi(int numberOfMonteCarloComputations, int numberOfSimulatedPoints) {
		/*
		 * numberOfMonteCarloComputations and numberOfDrawings are inherited from
		 * MonteCarloExperiments
		 */
		this.numberOfMonteCarloComputations = numberOfMonteCarloComputations;
		this.numberOfDrawings = numberOfSimulatedPoints;
		// exactResult is directly inherited from MonteCarloExperimentsWithExactResult
		this.exactResult = Math.PI;
	}

	/**
	 * It computes a Monte Carlo approximation of pi as the area of a circle of
	 * radius 1, which is of course four times the area of the part of the circle in
	 * the first quadrant. You compute this area by a Monte Carlo method as the
	 * fraction of random, independent numbers (x,y) between 0 and 1, such that
	 * x^2+y^2<=1.
	 *
	 * @return the approximation of pi.
	 */
	public double piMonteCarlo() {
		int numberOfPOintsInsideCircle = 0;
		for (int i = 0; i < numberOfDrawings; i++) {
			double x = Math.random();// random double between 0 and 1
			double y = Math.random();// random double between 0 and 1
			if (x * x + y * y < 1) {
				numberOfPOintsInsideCircle += 1;
			}
		}
		// close to pi for a large number of simulations
		return 4.0 * numberOfPOintsInsideCircle / numberOfDrawings;
	}

	@Override
	protected void generateMonteCarloComputations() {
		monteCarloComputations = new double[numberOfMonteCarloComputations];
		for (int i = 0; i < numberOfMonteCarloComputations; i++) {
			monteCarloComputations[i] = piMonteCarlo();// pi_i
		}
	}
}
