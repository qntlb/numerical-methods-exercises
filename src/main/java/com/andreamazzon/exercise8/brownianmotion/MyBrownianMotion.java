package com.andreamazzon.exercise8.brownianmotion;
import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class provides the implementation of a discrete time possibly multi-dimensional Brownian motion. All the simulated
 * Brownian motions are supposed to be independent
 *
 * @author Andrea Mazzon
 *
 */
public class MyBrownianMotion {

	private TimeDiscretizationFromArray times;//look at the TimeDiscretizationFromArray class of the Finmath library

	private int numberOfFactors; //more than one if this is a multi-dimensional Brownian motion
	private int numberOfPaths; //number of simulations
	private double initialValue = 0;
	/*
	 * Matrices of RandomVariableFromDoubleArray types: dimensions are time length and number of factors.
	 * A random variable can be seen as a vector, so these matrices can be seen as 3-dimensional matrices,
	 * the third dimension being the number of simulations.
	 */
	private RandomVariableFromDoubleArray[][] brownianIncrements = null;
	private RandomVariableFromDoubleArray[][] brownianPaths = null;

	public MyBrownianMotion( // Constructor
			TimeDiscretizationFromArray timeDiscretization, int numberOfFactors,
			int numberOfPaths) {
		this.times = timeDiscretization;
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
	}
	// Overloaded constructor generating a time discretization internally from the given data
	public MyBrownianMotion(
			double initialTimeValue,
			int numberOfTimeSteps, //number of time steps of the time discretization
			double deltaT, //length of the time step (supposed HERE to be constant)
			int numberOfFactors,
			int numberOfPaths) {
		this.times = new TimeDiscretizationFromArray(initialTimeValue, numberOfTimeSteps,
				deltaT);
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
	}

	/*
	 * It generates a Brownian motion, i.e., it fills the entries of brownianIncrements and brownianPaths.
	 * In particular, it first creates 3-dimensional matrices of doubles and then wraps them in
	 * 2-dimensional matrices of objects of type RandomVariableFromDoubleArray.
	 */
	private void generateBrownianMotion() {
		/*
		 *  number of time steps: we get it through the getNumberOfTimeSteps method of
		 *  TimeDiscretizationFromArray
		 */
		int numberOfTimeSteps = times.getNumberOfTimeSteps();
		int numberOfTimes = times.getNumberOfTimes(); // number of TIMES = numberOfTimesteps + 1
		// 3-tensor. Dimensions are: number of time steps, number of factors and number of paths
		double[][][] brownianIncrements3Array = new double[numberOfTimeSteps][numberOfFactors]
				[numberOfPaths];
		// paths have numberOfTimeSteps + 1 points
		double[][][] brownianPaths3Array = new double[numberOfTimes][numberOfFactors]
				[numberOfPaths];
		NormalRandomVariable normalRv = new NormalRandomVariable(0.0, 1.0);

		double[] volatilities = new double[numberOfTimeSteps]; // allocate space for volatilities array
		for (int i = 0; i < volatilities.length; i++) {
			/*
			 * here is where the structure of the TimeDiscretisation is used: the mesh may not be
			 * equally sized.
			 */
			volatilities[i] = Math.sqrt(times.getTimeStep(i)); //other method of TimeDiscretizationFromArray!
		}

		/*
		 * TO DO: FILL THE FIELD brownianIncrements3Array and brownianPaths3Array BY A TRIPLE FOR LOOP.
		 * THE FIRST ROW IS GIVEN BY initialValue = 0. USE THE VOLATILITIES ABOVE AND THE OBJECT normalRv.
		 */


		/*
		 * At this point, we have two 3-dimensional matrices whose entries are the Brownian increments
		 * and values, respectively. Now we want to wrap them into 2-dimensional matrices of types
		 * RandomVariableFromDoubleArray: a vector containing all the realizations at a given time
		 * for a given Brownian factor is wrapped into an object of type RandomVariableFromDoubleArray.
		 */
		//First, we allocate memory for RandomVariableFromDoubleArray wrapper objects.
		brownianIncrements = new RandomVariableFromDoubleArray[numberOfTimeSteps]
				[numberOfFactors];
		brownianPaths = new RandomVariableFromDoubleArray[numberOfTimeSteps + 1]
				[numberOfFactors];

		/*
		 *  TO DO: WRAP brownianIncrements3Array AND brownianPaths3Array INTO brownianIncrements and
		 *  brownianPaths, WITH A DOUBLE FOR LOOP.
		 */
	}

	/**
	 * It gets and returns the time discretization used for the process
	 * @return the time discretization used for the process
	 */
	public TimeDiscretizationFromArray getTimeDiscretization() {
		return times;
	}

	/**
	 * It gets and returns the two-dimensional array of random variables
	 * representing the brownian increments. Dimensions are the number of factors and
	 * the number of times.
	 * @return the two-dimensional array of random variables
	 * representing the brownian increments
	 */
	public RandomVariableFromDoubleArray[][] getBrownianIncrements() {
		//IMPLEMENT THE METHOD
		return null;
	}

	/**
	 * It gets and returns the two-dimensional array of random variables
	 * representing the brownian realized paths. Dimensions are the number of factors and
	 * the number of times.
	 * @return the two-dimensional array of random variables
	 * representing the brownian paths
	 */
	public RandomVariableFromDoubleArray[][] getAllThePaths() {
		//IMPLEMENT THE METHOD
		return null;
	}

	/**
	 * It gets and returns a one-dimensional array of random variables representing
	 * the paths of the Brownian motion for a given factor.
	 * @param factor, index for the factor
	 * @return one-dimensional array of RandomVariableFromDoubleArray objects representing the evolution in time of
	 * the given indexed factor
	 */
	public RandomVariableFromDoubleArray[] getPathsForFactor(int factor) {
		//IMPLEMENT THE METHOD
		return null;
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing
	 * the given path of the Brownian motion for a given factor
	 * @param factor, index for the factor
	 * @param path, index for the path (i.e., a given simulation)
	 * @return a vector of doubles with the values of the path over time
	 */
	public double[] getSpecificPath(int factor, int path) {
		//IMPLEMENT THE METHOD
		return null;
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing
	 * the given increments of the Brownian motion for a given factor
	 * @param factor, index for the factor
	 * @param path, index for the path (i.e., a given simulation)
	 * @return a vector of doubles with the values of the increments over time
	 */
	public RandomVariableFromDoubleArray getBrownianIncrement(int timeIndex, int factor) {
		//IMPLEMENT THE METHOD
		return null;
	}

	/**
	 * It gets and returns a random variable which stands for the Brownian motion for a given factor at a given time
	 * @param factor, index for the factor
	 * @param timeIndex, index for the time at which the Brownian motion is considered
	 * @return a random variable which stands for the Brownian motion for a given factor at a given time
	 */
	public RandomVariableFromDoubleArray getSimulations(int timeIndex, int factor) {
		//IMPLEMENT THE METHOD
		return null;
	}

	/**
	 * It prints the path of the Brownian motion for a given simulation and a given factor
	 * @param factor, index for the factor
	 * @param path, index for the path (i.e., a given simulation)
	 */
	public void printPath(int path, int factor) {
		//IMPLEMENT THE METHOD
	}

	/**
	 * It prints the increment of the Brownian motion for a given simulation and a given factor
	 * at a given time
	 * @param timeindex, time at which the increment is considered
	 * @param factor, index for the factor
	 * @param path, index for the path (i.e., a given simulation)
	 */
	public void printIncrement(int timeindex, int factor, int path) {
		//IMPLEMENT THE METHOD
	}
}

