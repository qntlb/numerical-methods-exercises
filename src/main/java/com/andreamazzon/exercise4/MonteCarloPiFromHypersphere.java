package com.andreamazzon.exercise4;

import com.andreamazzon.exercise3.montecarlo.MonteCarloExperimentsWithExactResult;

/**
 * This class deals with the approximation of Pi by Monte-Carlo. In particular,
 * pi is computed from the approximation of the volume of a unit hypersphere in
 * d dimensions by the integration of the indicator \[ 1_{\{x_1^2+x_2^+...+x_d^2
 * <= 1\}\], see equation (1) of the exercise sheet. This class extends
 * MonteCarloExperimentsWithExactResult in
 * com.andreamazzon.exercise3.montecarlo, so it's possible to get statistics on
 * the error by calling the inherited methods getAbsoluteErrorsOfComputations()
 * and getAverageAbsoluteError()
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloPiFromHypersphere extends MonteCarloExperimentsWithExactResult {

	private int dimension;// dimension of the unit hyperpshere

	public MonteCarloPiFromHypersphere(int numberOfMonteCarloComputations, int numberOfSimulatedPoints, int dimension) {
		/*
		 * numberOfMonteCarloComputations and numberOfDrawings are inherited from
		 * MonteCarloExperiments
		 */
		this.numberOfMonteCarloComputations = numberOfMonteCarloComputations;
		this.numberOfDrawings = numberOfSimulatedPoints;
		/*
		 * the field exactResult is directly inherited from
		 * MonteCarloExperimentsWithExactResult, and is initialized here.
		 */
		this.exactResult = Math.PI;
		this.dimension = dimension;// specific field of this class
	}

	/*
	 * used in order to compute the approximation of pi from the one of the volume
	 * of the unit hypersphere
	 */
	private static int factorial(int n) {
		if (n < 1) {
			return 1; // note: return already exits the method. No need for else
		}
		return n * factorial(n - 1);
	}

	/*
	 * Here we use the formulas V_{2k} = pi^k/k!, V_{2k+1}=2(4*pi)^k*k!/(2k+1)!,
	 * where V_n is the volume of the unit hypersphere in n dimensions
	 */
	private double computePiFromVolume(double volume) {
		double approximatedPi; // only definition here, initialization in the if/else loop
		if (dimension % 2 == 0) { // V_{2k} = pi^k/k!
			int k = dimension / 2;
			approximatedPi = Math.pow(volume * factorial(k), 1.0 / k);
		} else {// V_{2k+1}=2(4*pi)^k*k!/(2k+1)!
			int k = (dimension - 1) / 2;
			approximatedPi = Math.pow(volume * factorial(dimension) / (2.0 * factorial(k)), 1.0 / k) / 4.0;
		}
		return approximatedPi;
	}

	/**
	 * It computes a Monte Carlo approximation of pi from the volume of a unit
	 * hypersphere of dimension d. You compute this volume by a Monte Carlo method
	 * as 2^d times the fraction of random, independent numbers (x_1,..,x_d)
	 * uniformly distributed between 0 and 1, such that
	 * (2*(x_1-0.5))^2+...+(2*(x_d-0.5))^2<=1.
	 *
	 * @return the approximation of pi.
	 */
	public double piMonteCarloGeneralDimension() {
		int numberOfPointsInsideHypersphere = 0;
		for (int i = 0; i < numberOfDrawings; i++) {
			/*
			 * at every iteration, sample (x_1,...,x_d) and compute
			 * (2*(x_1-0.5))^2+...+(2*(x_d-0.5))^2
			 */
			double sumOfSquares = 0;
			for (int j = 0; j < dimension; j++) {
				double newRandom = 2 * (Math.random() - 0.5);
				sumOfSquares += newRandom * newRandom;
			}
			/*
			 * check if the sum is less or equal 1, i.e., if (x_1,...,x_d) is inside the
			 * unit hypersphere
			 */
			if (sumOfSquares <= 1) {
				numberOfPointsInsideHypersphere += 1;
			}
		}
		double volumeApproximation = Math.pow(2.0, dimension) * numberOfPointsInsideHypersphere / numberOfDrawings;
		return computePiFromVolume(volumeApproximation);
	}

	@Override
	protected void generateMonteCarloComputations() {
		monteCarloComputations = new double[numberOfMonteCarloComputations];
		for (int i = 0; i < numberOfMonteCarloComputations; i++) {
			monteCarloComputations[i] = piMonteCarloGeneralDimension();// pi_i
		}
	}
}
