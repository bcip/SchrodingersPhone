package com.github.bcip.schrodingersphone.sensor;

import android.hardware.SensorEvent;

import com.github.bcip.schrodingersphone.Feature;

/**
 * 
 * This wrapper extracts the feature from the data
 * 
 * @author wjmzbmr
 */

public class FeatureWrapper {// wrapper feature data
	double[][] data = new double[3][Feature.SEQUENCE_LENGTH];
	int cur;

	void clear() {
		cur = 0;
	}

	boolean isFull() {
		return cur == Feature.SEQUENCE_LENGTH;
	}

	/**
	 * Because the data order in our sensor and the dataset is different, we
	 * should swap the order of y and z
	 * 
	 * @param e
	 */
	void addData(SensorEvent e) {
		if (isFull())
			return;
		double x = e.values[0];
		double y = e.values[1];
		double z = e.values[2];
		data[0][cur] = x;
		data[1][cur] = z;
		data[2][cur] = y;
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