package hu.bme.aut.sensorinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MagFragment extends Fragment
{
	private static SensorManager sensorService;
	private Sensor sensor;
	private DrawingView canvas;
	private SharedPreferences sp;
	private String unit_magnet;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		return inflater.inflate(R.layout.mag_fragment, container, false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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
		unit_magnet = sp.getString("unit_magnet", "μT");
		createSpecs();
		canvas = getView().findViewById(R.id.Mag_sensorCanvas);
		canvas.setRange(sensor.getMaximumRange());

	}

	private void createSpecs()
	{
		FrameLayout specs = getView().findViewById(R.id.Mag_sensorSpecs);
		View sub_specs = getLayoutInflater().inflate(R.layout.sub_sensor_specs,null);
		specs.addView(sub_specs);

		TextView ventor = getView().findViewById(R.id.sensorFragmentVentorName);
		ventor.setText(sensor.getVendor());

		TextView version = getView().findViewById(R.id.sensorFragmentVersionName);
		version.setText(sensor.getVersion()+ " ");

		TextView maxRange = getView().findViewById(R.id.sensorFragmentMaxrangeName);
		maxRange.setText((unit_magnet.equals("mG")? convertuTtomG(sensor.getMaximumRange()) : sensor.getMaximumRange()) + unit_magnet);

		TextView resolution = getView().findViewById(R.id.sensorFragmentResolutionName);
		resolution.setText((unit_magnet.equals("mG")? convertuTtomG(sensor.getResolution()) : sensor.getResolution())+ unit_magnet);

		TextView current = getView().findViewById(R.id.sensorFragmentPowerName);
		current.setText(sensor.getPower()+ "mA");

	}


	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			{
				TextView sensorValue = getView().findViewById(R.id.sensorFragmentValue);
				sensorValue.setText(
						"x: "+ (unit_magnet.equals("rad")? convertuTtomG(event.values[0]): event.values[0]) + unit_magnet + "\n" +
						"y: "+ (unit_magnet.equals("rad")? convertuTtomG(event.values[1]): event.values[1]) + unit_magnet + "\n" +
						"z: "+ (unit_magnet.equals("rad")? convertuTtomG(event.values[2]): event.values[2]) + unit_magnet);
				canvas.addPos(event.values[0],event.values[1],event.values[2]);
				canvas.invalidate();
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	};


	private float convertuTtomG(float uT)
	{
		return (uT * 10);
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
