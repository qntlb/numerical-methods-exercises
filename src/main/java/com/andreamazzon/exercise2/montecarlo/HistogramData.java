package com.andreamazzon.exercise2.montecarlo;

public class HistogramData {

	private int[] histogram;

	private double minBin;
	private double maxBin;

	public int[] getHistogram() {
		return histogram;
	}

	public void setHistogram(int[] histogram) {
		this.histogram = histogram;
	}

	public double getMinBin() {
		return minBin;
	}

	public void setMinBin(double minBin) {
		this.minBin = minBin;
	}

	public double getMaxBin() {
		return maxBin;
	}

	public void setMaxBin(double maxBin) {
		this.maxBin = maxBin;
	}

}
