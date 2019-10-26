package hu.bme.aut.sensorinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccFragment extends Fragment
{
	private static SensorManager sensorService;
	private Sensor sensor;
	private DrawingView canvas;
	private SharedPreferences sp;
	private String unit_length;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		return inflater.inflate(R.layout.acc_fragment, container, false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (sensor != null) {
			sensorService.registerListener(sensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);

		}

		sp = PreferenceManager.getDefaultSharedPreferences(getContext());

	}

	@Override
	public void onResume()
	{
		super.onResume();
		if(sp.getString("unit_length", "m-cm").equals("m-cm"))
		{
			unit_length = "m/s^2";
		}
		else
		{
			unit_length = "ft/s^2";
		}
		createSpecs();
		canvas = getView().findViewById(R.id.Acc_sensorCanvas);
		canvas.setRange(sensor.getMaximumRange());
	}

	private void createSpecs()
	{
		FrameLayout specs = getView().findViewById(R.id.Acc_sensorSpecs);
		View sub_specs = getLayoutInflater().inflate(R.layout.sub_sensor_specs,null);
		specs.addView(sub_specs);

		TextView ventor = getView().findViewById(R.id.sensorFragmentVentorName);
		ventor.setText(sensor.getVendor());

		TextView version = getView().findViewById(R.id.sensorFragmentVersionName);
		version.setText(sensor.getVersion()+ " ");

		TextView maxRange = getView().findViewById(R.id.sensorFragmentMaxrangeName);
		maxRange.setText((unit_length.equals("ft/s^2")? convertMtoFt(sensor.getMaximumRange()) : sensor.getMaximumRange()) + unit_length);

		TextView resolution = getView().findViewById(R.id.sensorFragmentResolutionName);
		resolution.setText((unit_length.equals("ft/s^2")? convertMtoFt(sensor.getResolution()) : sensor.getResolution()) + unit_length);

		TextView current = getView().findViewById(R.id.sensorFragmentPowerName);
		current.setText(sensor.getPower()+ "mA");

	}


	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
				TextView sensorValue = getView().findViewById(R.id.sensorFragmentValue);
				if(unit_length.equals("ft/s^2"))
				{
					sensorValue.setText("x: "+ convertMtoFt(event.values[0])+ unit_length + "\n" +
										"y: " + convertMtoFt(event.values[1]) + unit_length + "\n" +
										"z: " + convertMtoFt(event.values[2]) + unit_length);
				}
				else
				{
					sensorValue.setText("x: " + event.values[0] + unit_length + "\n" +
										"y: "+ event.values[1] + unit_length + "\n" +
										"z: "+event.values[2] + unit_length);
				}

				canvas.addPos(event.values[0],event.values[1],event.values[2]);
				canvas.invalidate();
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	};


	private float convertMtoFt(float m)
	{
		return (m * 3.2808399f);
	}


	@Override
	public void onDestroyView()
	{
		if (sensor != null) {
			sensorService.unregisterListener(sensorEventListener);
		}
		super.onDestroyView();
	}
}
