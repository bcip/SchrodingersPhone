package com.github.bcip.schrodingersphone.sensor;

import android.hardware.SensorEvent;

import com.github.bcip.schrodingersphone.Feature;

public class FeatureWrapper {// wrapper feature data
	double[][] data = new double[3][Feature.FEATURE_LENGTH];
	int cur;

	void clear() {
		cur = 0;
	}

	boolean isFull() {
		return cur == Feature.FEATURE_LENGTH;
	}

	void addData(SensorEvent e) {
		if (isFull())
			return;
		for (int i = 0; i < 3; i++) {
			data[i][cur] = e.values[i];
		}
		cur++;
	}

	Feature build() {
		if (!isFull())
			return null;
		Feature ret = new Feature(data);
		clear();
		return ret;
	}
}