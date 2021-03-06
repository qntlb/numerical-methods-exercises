package com.andreamazzon.exercise9.approximationschemes;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class simulates the trajectories of a geometric Brownian motion (i.e., Black-Scholes model)
 * by using an Euler scheme. It extends AbstractSimulation by giving the implementation of
 * getDrift and getDiffusion.
 *
 * @author Andrea Mazzon
 */
public class EulerSchemeForBlackScholes extends AbstractSimulation {

	private final double muDrift;//mu
	private final double sigmaVolatility;//sigma

	public EulerSchemeForBlackScholes(int numberOfSimulations, double sigmaVolatility, double muDrift,
			double initialValue, int seed, TimeDiscretization times) {
		this.numberOfSimulations = numberOfSimulations;
		this.muDrift = muDrift;
		this.sigmaVolatility = sigmaVolatility;
		this.initialValue = initialValue;
		this.seed = seed;
		this.times = times;
		this.transform = (x -> x);
		this.inverseTransform = (x -> x);
	}

	/*
	 * It gets and returns the drift of a geometric Brownian motion computed with the Euler scheme.
	 * That is, it returns mu*S_{t_{k-1}}*(t_k-t_{k-1}). Here S_{t_{k-1}} is given
	 * as an argument, called lastRealization.
	 */
	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		final double timeStep = times.getTimeStep(timeIndex - 1);
		return lastRealization.mult(muDrift).mult(timeStep);
	}

	/*
	 * It gets and returns the diffusion of a geometric Brownian motion computed with the Euler scheme.
	 * That is, it returns sigma*S_{t_{k-1}}*(W_{t_k}-W_{t_{k-1}). Here S_{t_{k-1}} is given
	 * as an argument, called lastRealization.
	 */
	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		final RandomVariable brownianIncrement = brownianMotion.getBrownianIncrement(timeIndex - 1, 0);
		return lastRealization.mult(sigmaVolatility).mult(brownianIncrement);
	}

}

