package com.andreamazzon.exercise8.brownianmotion;
import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class provides the implementation of a discrete time possibly
 * multi-dimensional Brownian motion. All the simulated Brownian motions are
 * supposed to be independent. A one-dimensional Brownian motion is simulated
 * for a time discretization (t_0,t_1,..,t_n), identified by a
 * TimeDiscretizationFromArray object, and is represented by a one-dimensional
 * array of objects of type RandomVariableFromDoubleArray. In order to simulate
 * the process itself, we simulate the brownian increments ΔB_j, j = 1,...,n,
 * having distribution N(0, Δ_j) with Δ_j:= t_j-t_{j-1}. Then we simply go
 * forward putting B_{t_j}= B_{t_{j-1}} + ΔB_j.
 *
 * @author Andrea Mazzon
 *
 */
public class MyBrownianMotion {

	private final TimeDiscretization times;//look at the TimeDiscretizationFromArray class of the Finmath library

	private final int numberOfFactors; //more than one if this is a multi-dimensional Brownian motion
	private final int numberOfPaths; //number of simulations
	private final double initialValue = 0;
	/*
	 * Matrices of RandomVariableFromDoubleArray types: dimensions are time length
	 * and number of factors. A random variable can be seen as a vector, so these
	 * matrices can be seen as 3-dimensional matrices of doubles, the third
	 * dimension being the number of simulations.
	 */
	private RandomVariable[][] brownianIncrements;
	private RandomVariable[][] brownianPaths;

	public MyBrownianMotion( // Constructor
			TimeDiscretization timeDiscretization, int numberOfFactors,
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
		final int numberOfTimeSteps = times.getNumberOfTimeSteps();
		final int numberOfTimes = times.getNumberOfTimes(); // number of TIMES = numberOfTimesteps + 1
		// 3-tensor. Dimensions are: number of time steps, number of factors and number of paths
		final double[][][] brownianIncrements3Array = new double[numberOfTimeSteps][numberOfFactors]
				[numberOfPaths];
		// paths have numberOfTimeSteps + 1 points
		final double[][][] brownianPaths3Array = new double[numberOfTimes][numberOfFactors]
				[numberOfPaths];
		final NormalRandomVariable normalRv = new NormalRandomVariable(0.0, 1.0);

		final double[] volatilities = new double[numberOfTimeSteps]; // allocate space for volatilities array
		for (int i = 0; i < numberOfTimeSteps; i++) {
			/*
			 * here is where the structure of the TimeDiscretisation is used: the mesh may not be
			 * equally sized.
			 */
			volatilities[i] = Math.sqrt(times.getTimeStep(i)); //other method of TimeDiscretizationFromArray!
		}

		// loop: Generate uncorrelated Brownian increments
		for (int pathIndex = 0; pathIndex < numberOfPaths; pathIndex++) {
			for (int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
				// first we fill the entries of the 3-dimensional matrix of doubles
				brownianPaths3Array[0][factorIndex][pathIndex] = initialValue;
				for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
					brownianIncrements3Array[timeIndex][factorIndex][pathIndex] = normalRv.generate()
							* volatilities[timeIndex];
					// we sum the increment
					brownianPaths3Array[timeIndex
					                    + 1][factorIndex][pathIndex] = brownianPaths3Array[timeIndex][factorIndex][pathIndex]
					                    		+ brownianIncrements3Array[timeIndex][factorIndex][pathIndex];
				}
			}
		}


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

		// Wrap the values in RandomVariable objects
		for (int j = 0; j < numberOfFactors; j++) {
			/*
			 * The entries for time equal to zero are actually non stochastic: we use an
			 * overload version of the constructor of RandomVariableFromDoubleArray that
			 * builds non stochastic random variables (i.e., all the realizations are the
			 * same).
			 */
			brownianPaths[0][j] = new RandomVariableFromDoubleArray(0, // filtration time
					initialValue);
			for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
				brownianIncrements[timeIndex][j] = new RandomVariableFromDoubleArray(times.getTime(timeIndex),
						brownianIncrements3Array[timeIndex][j]);// vector: realizations of the rv
				brownianPaths[timeIndex + 1][j] = new RandomVariableFromDoubleArray(times.getTime(timeIndex + 1),
						brownianPaths3Array[timeIndex + 1][j]); // be careful on the indexes here
			}
		}
	}

	/**
	 * It gets and returns the time discretization used for the process
	 * @return the time discretization used for the process
	 */
	public TimeDiscretization getTimeDiscretization() {
		return times;
	}

	/**
	 * It gets and returns the two-dimensional array of random variables
	 * representing the brownian increments. Dimensions are the number of factors
	 * and the number of times.
	 *
	 * @return the two-dimensional array of random variables representing the
	 *         brownian increments
	 */
	public RandomVariable[][] getBrownianIncrements() {
		// lazy initialization: brownianIncrements gets initialized only when needed
		if (brownianIncrements == null) { // generated only once
			generateBrownianMotion();
		}
		return brownianIncrements;
	}

	/**
	 * It gets and returns the two-dimensional array of random variables
	 * representing the brownian realized paths. Dimensions are the number of factors and
	 * the number of times.
	 * @return the two-dimensional array of random variables
	 * representing the brownian paths
	 */
	public RandomVariable[][] getAllThePaths() {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianPaths == null) { // generated only once
			generateBrownianMotion();
		}
		return brownianPaths;
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing the given
	 * increments of the Brownian motion for a given factor
	 *
	 * @param factor, index for the factor
	 * @param path,   index for the path (i.e., a given simulation)
	 * @return a vector of doubles with the values of the increments over time
	 */
	public RandomVariable getBrownianIncrement(int timeIndex, int factorIndex) {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianIncrements == null) { // generated only once
			generateBrownianMotion();
		}
		return brownianIncrements[timeIndex][factorIndex];
	}

	/**
	 * It gets and returns a random variable which stands for the Brownian motion
	 * for a given factor at a given time
	 *
	 * @param factor,    index for the factor
	 * @param timeIndex, index for the time at which the Brownian motion is
	 *                   considered
	 * @return a random variable which stands for the Brownian motion for a given
	 *         factor at a given time
	 */
	public RandomVariable getSimulations(int timeIndex, int factorIndex) {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianPaths == null) { // generated only once
			generateBrownianMotion();
		}
		return brownianPaths[timeIndex][factorIndex];
	}

	/**
	 * It gets and returns a one-dimensional array of random variables representing
	 * the paths of the Brownian motion for a given factor.
	 *
	 * @param factor, index for the factor
	 * @return one-dimensional array of RandomVariableFromDoubleArray objects
	 *         representing the evolution in time of the given indexed factor
	 */
	public RandomVariable[] getPathsForFactor(int factorIndex) {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianPaths == null) {// generated only once
			generateBrownianMotion();
		}
		final int numberOfTimes = times.getNumberOfTimes();
		final RandomVariable[] paths = new RandomVariableFromDoubleArray[numberOfTimes];
		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			paths[timeIndex] = brownianPaths[timeIndex][factorIndex];
		}
		return paths;
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing
	 * the given path of the Brownian motion for a given factor
	 * @param factor, index for the factor
	 * @param path, index for the path (i.e., a given simulation)
	 * @return a vector of doubles with the values of the path over time
	 */
	public double[] getSpecificPath(int factorIndex, int pathIndex) {
		// first we get the vector of random variables, for the given factor
		final RandomVariable[] paths = getPathsForFactor(factorIndex);
		final int numberOfTimes = paths.length;
		final double[] specificPath = new double[numberOfTimes];
		// the we extrapolate the path for the specific path
		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			specificPath[timeIndex] = paths[timeIndex].get(pathIndex); // get method of RandomVariableFromArray
		}
		return specificPath;
	}

	/**
	 * It prints the path of the Brownian motion for a given simulation and a given
	 * factor
	 *
	 * @param factor, index for the factor
	 * @param path,   index for the path (i.e., a given simulation)
	 */
	public void printSpecificPath(int factorIndex, int pathIndex) {
		final double[] specificPath = getSpecificPath(factorIndex, pathIndex);
		final int numberOfTimes = specificPath.length;

		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			System.out.println(specificPath[timeIndex]);
		}
	}

	/**
	 * It prints the increment of the Brownian motion for a given simulation and a given factor
	 * at a given time
	 * @param timeindex, time at which the increment is considered
	 * @param factor, index for the factor
	 * @param path, index for the path (i.e., a given simulation)
	 */
	public void printIncrement(int timeindex, int factorIndex, int pathIndex) {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianIncrements == null) { // generated only once
			generateBrownianMotion();
		}
		System.out.println(brownianIncrements[timeindex][factorIndex].get(pathIndex));
	}
}


