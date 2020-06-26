package com.andreamazzon.exercise9.approximationschemes;

import java.util.function.DoubleUnaryOperator;

import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This is an abstract class for the simulation of a stochastic process. The
 * only abstract methods are the ones that return the drift
 * mu(X_{i-1}, t_i)(t_i-t_{i-1})
 * and the diffusion
 * sigma(X_{i-1}, t_i)(t_i-t_{i-1})
 * of the process, since they can be implemented in different ways depending on the scheme
 * (for example, Euler, log-Euler or Milstein) and on the dynamics of the process.
 * All the other methods are implemented in this class, as they only depend on the generation
 * of the process.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class AbstractSimulation {
	//it will contain the paths of the process
	private RandomVariable[] paths;//not yet initialized: default value is null.

	//all these fields are protected; minimum level of visibility you can have, as it is a parent class
	protected int numberOfSimulations;

	protected TimeDiscretization times;

	protected double initialValue;

	//used in order to generate the Brownian motion
	protected int seed;

	//used as the stochastic driver of the process
	protected BrownianMotion brownianMotion;

	/*
	 * they are not the identity if it can be useful to use Ito's formula in order to simulate
	 * a convenient function of the process. In particular, transform gives the function we have
	 * to apply to the trajectories of the process we have simulated in order to get the one we want to return.
	 * For example, in the log Euler method this is the exponential function: we simulate the logarithm
	 * and we want to return anyway the process.
	 */
	protected DoubleUnaryOperator transform;
	protected DoubleUnaryOperator inverseTransform;//log for log Euler

	/*
	 * they of course depend on the process and on the scheme. We suppose that the drift
	 * and the diffusion are functions of the process and of the time.
	 */
	protected abstract RandomVariable getDrift(RandomVariable lastRealization, int timeIndex);
	protected abstract RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex);

	/*
	 * it actually generates the process
	 */
	private void generate() {

		final int numberOfTimes = times.getNumberOfTimes();

		/*
		 * one-dimensional Brownian motion. Note that it has a method
		 * getIncrement(final int timeIndex, final int factor)
		 * that must be called in the generation of the diffusion in the derived classes.
		 * So you don't really use it here, but in the derived classes.
		 */
		brownianMotion = new BrownianMotionFromMersenneRandomNumbers(times, 1, numberOfSimulations, seed);

		/*
		 * the drift and the diffusion of the process are random variables. We don't need to
		 * store them: they will be uploaded every time.
		 */
		RandomVariable processDrift;
		RandomVariable processDiffusion;
		paths = new RandomVariable[numberOfTimes];//one random variable every time

		paths[0] = new RandomVariableFromDoubleArray(times.getTime(0), initialValue);


		for (int timeIndex = 1; timeIndex < times.getNumberOfTimes(); timeIndex++) {
			/*
			 * for every time step, we compute drift and diffusion of the process, as RandomVariable
			 * objects, and we add them to the previous value of the process.
			 */
			processDrift = getDrift(paths[timeIndex - 1],timeIndex);
			processDiffusion = getDiffusion(paths[timeIndex - 1],timeIndex);
			paths[timeIndex] = paths[timeIndex - 1].apply(inverseTransform).add(processDrift).add(processDiffusion);
			paths[timeIndex] = paths[timeIndex].apply(transform);
		}
	}

	//getters

	/**
	 * It gets the initial value of the process, as a double
	 * @return  the initial value of the process
	 */
	public double getInitialValue() {
		return initialValue;
	}

	/**
	 * It returns the seed by which the Brownian motion is generated
	 * @return the seed by which the Brownian motion is generated
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * It returns the number of times in the time discretization
	 * @return the number of times in the time discretization
	 */
	public int getNumberOfTimes() {
		return times.getNumberOfTimes();
	}

	/**
	 * It returns the last time of the time discretization
	 * @return the last time of the time discretization
	 */
	public double getTimeHorizon() { // Just some getters and setters...
		return times.getTime(getNumberOfTimes());
	}

	/**
	 * It returns the number of paths of the process, i.e., the number of simulations
	 * @return the number of paths of the process
	 */
	public int getNumberOfSimulations() {
		return numberOfSimulations;
	}

	/**
	 * It returns the vector of random variables with the realizations of the process.
	 * It generates the process only if this has not already done.
	 * @return paths, vector of random variables with the realizations of the process.
	 */
	public RandomVariable[] getPaths() {
		if (paths == null) {
			generate();
		}
		return paths;
	}

	/**
	 * It returns a random variable with the realizations of the process at a give time index.
	 * It generates the process only if this has not already done.
	 * @param timeInstant, index of the time considered
	 * @return paths, vector of random variables with the realizations of the process.
	 */
	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex) { //return the whole realisation at time t
		if (paths == null) {
			generate();
		}
		return paths[timeIndex];
	}

	/**
	 * It returns a random variable with the realizations of the process at a given time.
	 * @param time, the value of the time considered
	 * @return a random variable with the realizations of the process.
	 */
	public RandomVariable getProcessAtGivenTime(double time) {
		return getProcessAtGivenTimeIndex(times.getTimeIndex(time));
	}

	/**
	 * It returns a vector of doubles representing a path of the process for a given simulation.
	 * @param pathNumber, index of the simulation we consider
	 * @return the path of the process for the given simulation index
	 */
	public double[] getPathForGivenSimulation(int pathNumber) {
		final RandomVariable[] pathAsRandomVariables = getPaths();
		final int numberOfTimes = times.getNumberOfTimes();
		final double samplePath[] = new double[numberOfTimes];
		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			samplePath[timeIndex] = pathAsRandomVariables[timeIndex].get(pathNumber);
		}
		return samplePath;
	}

	/**
	 * It prints a vector of doubles representing a path of the process for a given simulation.
	 * @param pathNumber, index of the simulation we consider
	 */
	public void printAPath(int pathNumber) {
		final double[] samplePath = getPathForGivenSimulation(pathNumber);
		for (final double realization : samplePath) {
			System.out.println(realization);
		}
	}

	/**
	 * It returns the final value of the process
	 * @return random variable holding the realizations of the process at final time
	 */
	public RandomVariable getFinalValue() {
		return getProcessAtGivenTimeIndex(times.getNumberOfTimes() - 1);
	}
}

