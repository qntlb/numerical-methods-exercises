package com.andreamazzon.exercise2.montecarlo;

import java.util.Random;

import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;
import com.andreamazzon.session5.abstractclasses.usingsimulators.BinomialModelUser;

/**
 * This class contains methods that are useful to test on the way the
 * Monte-Carlo method works. Here we consider the computation of the price of a
 * digital option with underlying given by a binomial model.
 * 
 * @author Andrea Mazzon
 *
 */
public class MonteCarloWithBinomial {

	// values for the simulation of the binomial model
	private double initialValue;
	private double increaseIfUp;
	private double decreaseIfDown;
	private int lastTime;
	/*
	 * the number of columns of the matrix where the realizations of the binomial
	 * model are stored
	 */
	private int numberOfSimulations;

	// threshold for the option
	private double threshold;
	// maturity for the option
	private int maturity;// the time is discrete, the time and the index coincide

	// used to give the random seed to the constructor of BinomialModelUser
	private Random random = new Random();

	MonteCarloWithBinomial(double initialValue, double increaseIfUp, double decreaseIfDown, int lastTime,
			int numberOfSimulations, double threshold, int maturity) {
		this.initialValue = initialValue;
		this.increaseIfUp = increaseIfUp;
		this.decreaseIfDown = decreaseIfDown;
		this.lastTime = lastTime;
		this.numberOfSimulations = numberOfSimulations;
		this.threshold = threshold;
		this.maturity = maturity;
	}

	/**
	 * It returns the Monte-Carlo price of a digital option with underlying given by
	 * by a binomial model, simulated witha given seed
	 * 
	 * @param seed, the seed given to the constructor of BinomialModelUser to
	 *              simulate the values of the process
	 * @return the price of the option
	 */
	public double getPriceForGivenSeed(int seed) {
		/*
		 * the class of the digital option has two fields: the underlying and the
		 * threshold. Overloaded constructor of the BinomialModelUser: interest rate
		 * equal to zero.
		 */
		BinomialModelUser binomialModelWithSpecifiedSeed = new BinomialModelUser(initialValue, increaseIfUp,
				decreaseIfDown, seed, lastTime, numberOfSimulations);
		DigitalOption digitalOptionForGivenSeed = new DigitalOption(binomialModelWithSpecifiedSeed, threshold);
		double price = digitalOptionForGivenSeed.getPrice(maturity);
		return price;
	}

	/*
	 * this method returns a one-dimensional array whose entries are the prices of
	 * the digital option, computed by giving to the constructor of
	 * BinomialModelUser a random seed for every entry of the array.
	 */
	private double[] getValuesDifferentSeeds(int numberOfPriceComputations) {
		/*
		 * numberOfPriceComputations is the length of the array: we get a different
		 * price for every iteration of the for loop
		 */
		double[] prices = new double[numberOfPriceComputations];
		for (int i = 0; i < numberOfPriceComputations; i++) {
			prices[i] = getPriceForGivenSeed(random.nextInt());
		}
		return prices;
	}

	/**
	 * It returns an object of a class storing min value, max value and a histogram
	 * for a one-dimensional array containing the prices of a digital option with
	 * underlying given by a binomial model with random seed. Every entry of the
	 * array is the price of the option for a given seed.
	 * 
	 * @param numberOfBins,        the number of bins of the histogram
	 * @param numberOfComputations
	 * @return an object of the container class for min value, max value and
	 *         histogram
	 */
	public HistogramData getHistogram(int numberOfBins, int numberOfPriceComputations) {
		// private method! only used to get the array, not from an user
		double[] prices = getValuesDifferentSeeds(numberOfPriceComputations);
		// minimum price
		double minPrice = UsefulMethodsMatricesVectors.getMin(prices);
		// maximum price
		double maxPrice = UsefulMethodsMatricesVectors.getMax(prices);
		// histogram
		int[] histogram = UsefulMethodsMatricesVectors.buildHistogram(prices, minPrice, maxPrice, numberOfBins);
		HistogramData histogramData = new HistogramData();
		// we call the setters of the container class
		histogramData.setHistogram(histogram);
		histogramData.setMinBin(minPrice);
		histogramData.setMaxBin(maxPrice);
		return histogramData;
	}

	/**
	 * It returns min value and max value of a a one-dimensional array containing
	 * the prices of a digital option with underlying given by a binomial model with
	 * random seed. Every entry of the array is the price of the option for a given
	 * seed.
	 * 
	 * @param numberOfComputations
	 * @return an array with min value and max value
	 */
	public double[] getMinAndMax(int numberOfPriceComputations) {
		// private method! only used to get the array, not from an user
		double[] prices = getValuesDifferentSeeds(numberOfPriceComputations);
		// minimum price
		double minPrice = UsefulMethodsMatricesVectors.getMin(prices);
		// maximum price
		double maxPrice = UsefulMethodsMatricesVectors.getMax(prices);
		double[] minAndMax = { minPrice, maxPrice };
		return minAndMax;
	}
}
