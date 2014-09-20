package com.github.bcip.schrodingersphone.sensor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import com.example.t111.R;
import com.github.bcip.schrodingersphone.Feature;
import com.github.bcip.schrodingersphone.Predictor;
import com.github.bcip.schrodingersphone.Record;
import com.github.bcip.schrodingersphone.SPException;
import com.github.bcip.schrodingersphone.State;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * This class implements the Android client for extracting the feature from the
 * user data collected by sensor at 20Hz. The feature is extracted for every 200
 * samples.
 */

public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private boolean color = false;
	private View view;
	private long lastUpdate;
	FeatureWrapper wrapper = new FeatureWrapper();
	Uploader uploader = new Uploader(ServerInfo.serverAddress, ServerInfo.port,
			this);
	Predictor predictor = new Predictor();

	Feature featureInDoubt = null;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = findViewById(R.id.textView);
		view.setBackgroundColor(Color.GREEN);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lastUpdate = System.currentTimeMillis();

		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// displayADialog(State.Walking, State.Jogging);
		InputStream inputStream = getResources().openRawResource(R.raw.coef);

		try {
			predictor.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// uploader.uploadRecord(new Record("Lijie", new Date(), "Walking"));
		// Toast.makeText(this, "Succeed", Toast.LENGTH_SHORT).show();
		// // showMessage(predictor.predict(ret).toString());
		// } catch (SPException e) {
		// Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
		// }

		// Toast.makeText(this, ret.toString(), Toast.LENGTH_SHORT).show();
	}

	/**
	 * This method will show a dialog to ask what is the user's actions from two
	 * choice of high probability.
	 * 
	 * It is used to listen to the user's feedback
	 * 
	 * @param a
	 * @param b
	 */
	public void displayADialog(final State a, final State b) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.btn_star).setTitle("Feedback")
				.setMessage("What is your last action?");

		builder.setPositiveButton(a.toString(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		builder.setNeutralButton(b.toString(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		builder.setNegativeButton("Neither", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// sent nothing
			}
		});

		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			getAccelerometer(event);
		}
	}

	void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 * This method add the event we have got to the FeatureWrapper, when the
	 * wrapper get enough data, it build the feature and send it to the server.
	 * 
	 * If the sending failed, it shows a 'Failed' message on the screen.
	 * 
	 * @param event
	 */
	private void getAccelerometer(SensorEvent event) {
		System.out.println(event.timestamp + Arrays.toString(event.values));

		wrapper.addData(event);
		if (wrapper.isFull()) {
			// sent the data
			Feature ret = wrapper.build();
			try {
				uploader.uploadRecord(new Record("Lijie", new Date(), ret));
				// Toast.makeText(this, "Succeed", Toast.LENGTH_SHORT).show();
				// showMessage(predictor.predict(ret).toString());
			} catch (SPException e) {
				Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
			} finally {
				showMessage(predictor.predict(ret).toString());
			}
		}
	}

	/**
	 * 50000 corresponds to 0.05s, which is the time gap for our feature.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				50000);
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}