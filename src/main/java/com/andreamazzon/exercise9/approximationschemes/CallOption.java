package com.andreamazzon.exercise9.approximationschemes;

/**
 * In this class we compute the price of a discounted call option with a given underlying
 * stochastic process, represented by an object of type AbstractSimulation
 *
 * @author Andrea Mazzon
 *
 */
public class CallOption {

	private final AbstractSimulation underlying;

	public CallOption(AbstractSimulation underlying) {
		this.underlying = underlying;
	}

	/**
	 * Computes and returns the price of the discounted call option.
	 * @param strike, the strike of the option
	 * @param maturity, the maturity of the option
	 * @param riskFreeRate, the risk free rate
	 * @return the price of the option
	 */
	public double priceCall(double strike, double maturity, double riskFreeRate) {
		/*
		 * TO DO: implement the method and the return the price as the average of the
		 * discounted payoff (here you suppose that you get the price at time 0),
		 * which is a function of the value of underlying at maturity time.
		 * Hint: you can use the method
		 * RandomVariable net.finmath.stochastic.RandomVariable.floor(double floor)
		 */
		return 0;
	}

}
