package com.github.bcip.schrodingersphone;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Predictor {

	double[][] data = new double[State.NUM_STATES][Feature.FEATURE_LENGTH];

	public Predictor() {
		// TODO Auto-generated constructor stub
	}

	public void load(String fileName) throws IOException {
		Scanner sc = new Scanner(new File(fileName));
		for (int i = 0; i < State.NUM_STATES; ++i) {
			for (int j = 0; j < Feature.FEATURE_LENGTH; j++) {
				data[i][j] = sc.nextDouble();
			}
		}
	}

	public State predict(Feature feature) {
		double[] v = feature.make();
		int by = -1;
		double mx = Double.MIN_VALUE;

		for (int i = 0; i < State.NUM_STATES; i++) {
			double dot = 0;
			for (int j = 0; j < Feature.FEATURE_LENGTH; ++j) {
				dot += v[j] * data[i][j];
			}
			if (dot > mx) {
				mx = dot;
				by = i;
			}
		}

		return State.values()[by];
	}
}
