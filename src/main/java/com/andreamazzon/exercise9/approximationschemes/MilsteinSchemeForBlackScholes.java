package com.andreamazzon.exercise9.approximationschemes;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class simulates the trajectories of a geometric Brownian motion (i.e., Black-Scholes model)
 * by using an Milstein scheme.
 *
 * @author Andrea Mazzon
 */
public class MilsteinSchemeForBlackScholes extends AbstractSimulation {

	public MilsteinSchemeForBlackScholes(int numberOfSimulations, double sigmaVolatility, double muDrift,
			double initialValue, int seed, TimeDiscretization times) {
		this.numberOfSimulations = numberOfSimulations;
		this.sigmaVolatility = sigmaVolatility;
		this.muDrift = muDrift;
		this.initialValue = initialValue;
		this.seed = seed;
		this.times = times;
		this.transform = (x -> x);
		this.inverseTransform = (x -> x);
	}

	/*
	 * It gets and returns the drift of a geometric Brownian motion computed with the Milstein scheme.
	 * That is, it returns mu*S_{t_{k-1}}*(T_k-t_{k-1}). Here S_{t_{k-1}} is given
	 * as an argument, called lastRealization.
	 */
	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		// TO DO:implement the method
		return  null;
	}

	/*
	 * It gets and returns the diffusion of a geometric Brownian motion computed with the Milstein scheme.
	 */
	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		// TO DO:implement the method, having a look at the scheme in the script
		return  null;
	}
}
