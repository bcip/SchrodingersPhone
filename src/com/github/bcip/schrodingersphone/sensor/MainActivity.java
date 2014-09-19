package com.github.bcip.schrodingersphone.sensor;

import java.util.Date;

import com.example.t111.R;
import com.github.bcip.schrodingersphone.Feature;
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

public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private boolean color = false;
	private View view;
	private long lastUpdate;
	FeatureWrapper wrapper = new FeatureWrapper();
	Uploader uploader = new Uploader(ServerInfo.serverAddress, ServerInfo.port);

	Feature featureInDoubt = null;

	/** Called when the activity is first created. */

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

		displayADialog(State.Walking, State.Jogging);

		// Toast.makeText(this, ret.toString(), Toast.LENGTH_SHORT).show();
	}

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
			try {
				getAccelerometer(event);
			} catch (SPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getAccelerometer(SensorEvent event) throws SPException {
		wrapper.addData(event);
		if (wrapper.isFull()) {
			// sent the data
			Feature ret = wrapper.build();
			uploader.uploadRecord(new Record("Lijie", new Date(), ret));
		}

		// float[] values = event.values;
		// // Toast.makeText(this, "" + event.timestamp,
		// // Toast.LENGTH_SHORT).show();
		// // Movement
		// float x = values[0];
		// float y = values[1];
		// float z = values[2];
		//
		// float accelationSquareRoot = (x * x + y * y + z * z)
		// / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		// long actualTime = event.timestamp;
		// if (accelationSquareRoot >= 2) //
		// {
		// if (actualTime - lastUpdate < 200) {
		// return;
		// }
		// lastUpdate = actualTime;
		// // Toast.makeText(this, x + " " + y + " " + z, Toast.LENGTH_SHORT)
		// // .show();
		// if (color) {
		// view.setBackgroundColor(Color.GREEN);
		// } else {
		// view.setBackgroundColor(Color.RED);
		// }
		// color = !color;
		// }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		sensorManager.unregisterListener(this);
	}
}