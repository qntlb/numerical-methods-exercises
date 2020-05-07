package com.andreamazzon.exercise2.montecarlo;

/**
 * This class deals with the implementation of a digital option with underlying 
 * a general stochastic process. The realizations of the underlying stochastic
 * process are stored in a matrix. A digital option with underlying S with maturity 
 * T pays 1 if S(T) >= K and 0 otherwise, where K is a threshold. 
 * This class has two public methods besides the constructor: the method getPayoff 
 * returns a one-dimensional array whose entries are the realizations of the payoff, i.e.
 * its i-th entry is 1 if the entry of the matrix of the realizations of the process at 
 * the time index corresponding to T and simulation i is >= K and 0 vice versa.
 * The method getPrice returns the Monte-Carlo price of the option: since the realization
 * of the process at time T are independent on each other, the realizations of the
 * payoff are independent as well. For this reason, we can approximate the expectation
 * of the payoff with the average of the realizations of the payoff.
 *   
 *   
 */
import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;
import com.andreamazzon.session5.abstractclasses.usingsimulators.StochasticProcessUser;

public class DigitalOption {

	// we use it to get the realizations at time T
	private StochasticProcessUser stochasticProcessUser;
	private double threshold;// the threshold K

	public DigitalOption(StochasticProcessUser stochasticProcessUser, double threshold) {
		this.stochasticProcessUser = stochasticProcessUser;
		this.threshold = threshold;
	}

	/**
	 * It returns a one-dimensional array whose entries are the realizations of the
	 * payoff of the digital option for the underlying represented by
	 * stochasticProcessUser
	 * 
	 * @param maturityIndex, the index of the maturity
	 * @return the realizations of the payoff, as a one-dimensional array
	 */
	public double[] getPayoff(int maturityIndex) {
		// realizations of the process at the time indexed by maturityIndex
		double[] realizations = stochasticProcessUser.getRealizationsAtGivenTime(maturityIndex);
		int numberOfSimulations = realizations.length;// this is the length of the array we return
		double[] payoffAtGivenTime = new double[numberOfSimulations];
		for (int simulationIndex = 0; simulationIndex < numberOfSimulations; simulationIndex++) {
			// note: this is the ternary if-else operator
			payoffAtGivenTime[simulationIndex] = (realizations[simulationIndex] >= threshold) ? 1 : 0;
		}
		return payoffAtGivenTime;
	}

	/**
	 * It returns the Monte-Carlo price of the digital option for the underlying
	 * represented by stochasticProcessUser. This is done by taking the average of
	 * the one-dimensional array returned by getPayoff.
	 * 
	 * @param maturityIndex, the index of the maturity
	 * @return the price of the option, i.e., the average of the realizations of the
	 *         payoff
	 */
	public double getPrice(int maturityIndex) {
		return UsefulMethodsMatricesVectors.getAverage(getPayoff(maturityIndex));
	}

}
