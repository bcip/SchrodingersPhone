package com.github.bcip.schrodingersphone;

//import android.annotation.SuppressLint;

public class Feature {
	double averageResultantAcc;
	FeatureOneAxis[] axies;

	//@SuppressLint("Assert")
	public Feature(double data[][]) {
		// TODO Auto-generated constructor stub
		axies = new FeatureOneAxis[3];
		for (int i = 0; i < 3; ++i)
			axies[i] = new FeatureOneAxis(data[i]);

		int n = data[0].length;
		assert (n == 200);
		for (double[] a : data)
			assert (a.length == 200);
		

		double sum = 0;
		for (int i = 0; i < n; ++i) {
			double x = data[0][i], y = data[1][i], z = data[2][i];
			double v = Math.sqrt(x * x + y * y + z * z);
			sum += v;
		}
		averageResultantAcc = sum / n;
	}
}

class FeatureOneAxis {
	double average;
	double standardDeviation;
	double averageAbsoluteDifference;
	double[] binDistribution;
	double timeBetweenPeeks;

	//@SuppressLint("Assert")
	public FeatureOneAxis(double[] data) {
		// TODO Auto-generated constructor stub
		int n = data.length;
		assert (n == 200);
		double sum = 0;
		for (double x : data) {
			sum += x;
		}
		average = sum / n;
		sum = 0;
		for (double x : data) {
			sum += (x - average) * (x - average);
		}
		standardDeviation = Math.sqrt(sum / n);
		sum = 0;
		for (double x : data) {
			sum += Math.abs(x - average);
		}
		averageAbsoluteDifference = sum / n;

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (double x : data) {
			min = Math.min(min, x);
			max = Math.max(max, x);
		}

		binDistribution = new double[10];

		if (Math.abs(max - min) < 1e-5) {
			binDistribution[0] = 1;
			return;
		}

		for (double x : data) {
			double at = (x - min) / (max - min);
			int v = (int) at * 10;
			if (v >= 10)
				v = 9;
			binDistribution[v]++;
		}
		for (int i = 0; i < 10; ++i) {
			binDistribution[i] /= n;
		}

		evalutePeek(data);
	}

	void evalutePeek(double data[]) {
		// My naive algorithm here:
		// Get 3 peek in descending order, ignore the one which are too near to
		// the old one;
		int have = 0;
		int[] peeks = new int[3];
		int n = data.length;

		final int THRESHOLD = 20;

		while (have < 3) {
			int by = -1;

			for (int i = 0; i < n; ++i) {
				boolean ok = true;
				for (int j = 0; j < have; ++j) {
					if (Math.abs(i - peeks[j]) < THRESHOLD) {
						ok = false;
						break;
					}
				}
				if (ok) {
					if (by == -1 || data[i] < data[by]) {
						by = i;
					}
				}
			}

			peeks[have++] = by;
		}

		timeBetweenPeeks = 1.0 * (peeks[2] - peeks[0]) / 2;
	}
}

