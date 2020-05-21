/**
 * The code in this package regards the approximation of pi from the
 * approximation of the volume of the unit hypersphere, for a given dimension.
 * The volume is seen as the integral of the indicator \[
 * 1_{\{x_1^2+x_2^+...+x_d^2 <= 1\}\], see equation (1) of the exercise sheet.
 * In particular, the quality of the approximation is tested when the points
 * (x_1^i,\dots,x_d^i), i=1,..,d are sampled by Monte-Carlo and when they are
 * sampled by an Halton sequence.
 * 
 */
package com.andreamazzon.exercise4;