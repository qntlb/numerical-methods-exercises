/**
 * This package is devoted to the analysis of the results of Monte-Carlo
 * implementations for general problems. In particular, the interface
 * MonteCarloEvaluations provides methods that can be called in order to get the
 * vector of several Monte-Carlo approximations of a given quantity, the average
 * and the standard deviation of the vector, as well as its minimum and maximum
 * value and an histogram of its elements. It is implemented by the abstract
 * class MonteCarloExperiments. The abstract class
 * MonteCarloExperimentsWithExactResult extends MonteCarloExperiments, providing
 * methods to get analyze the error of the approximation in the case when we
 * know the exact result.
 */
package com.andreamazzon.exercise3.montecarlo;