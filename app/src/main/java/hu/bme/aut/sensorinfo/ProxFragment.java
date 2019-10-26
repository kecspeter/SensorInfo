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


public class ProxFragment extends Fragment
{
	private static SensorManager sensorService;
	private Sensor sensor;
	private int mode = 1;
	private SharedPreferences sp;
	private boolean unit_prox;
	private String unit_length;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.prox_fragment, container, false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		if (sensor != null) {
			sensorService.registerListener(sensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		sp = PreferenceManager.getDefaultSharedPreferences(getContext());
	}

	@Override
	public void onResume()
	{
		super.onResume();
		FrameLayout specs = getView().findViewById(R.id.Prox_sensorSpecs);
		View sub_specs = getLayoutInflater().inflate(R.layout.sub_sensor_specs,null);
		specs.addView(sub_specs);

		unit_prox = sp.getBoolean("proximity_mode", false);
		if(sp.getString("unit_length", "NA").equals("m-cm"))
		{
			unit_length = "cm";
		}
		else
		{
			unit_length = "in";
		}


		TextView ventor = getView().findViewById(R.id.sensorFragmentVentorName);
		ventor.setText(sensor.getVendor());

		TextView version = getView().findViewById(R.id.sensorFragmentVersionName);
		version.setText(sensor.getVersion()+ " ");

		TextView maxRange = getView().findViewById(R.id.sensorFragmentMaxrangeName);
		maxRange.setText(unit_length.equals("in")? convertCmtoIn(sensor.getMaximumRange())+ unit_length : sensor.getMaximumRange()+ unit_length);

		TextView resolution = getView().findViewById(R.id.sensorFragmentResolutionName);
		resolution.setText(unit_length.equals("in")? convertCmtoIn(sensor.getResolution())+ unit_length : sensor.getResolution()+ unit_length);

		TextView current = getView().findViewById(R.id.sensorFragmentPowerName);
		current.setText(sensor.getPower()+ "mA");

	}

	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if(event.sensor.getType() == Sensor.TYPE_PROXIMITY)
			{
				TextView sensorValue = getView().findViewById(R.id.sensorFragmentValue);
				if(unit_prox)
				{
					sensorValue.setText(unit_length.equals("in")? convertCmtoIn(event.values[0]) + unit_length : event.values[0] + unit_length);
				}
				else
				{
					if(event.values[0] == 0)
					{

						sensorValue.setText(getString(R.string.PROXIMITY_NEAR));
					}
					else
					{
						sensorValue.setText(getString(R.string.PROXIMITY_FAR));
					}
				}

			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	};

	private float convertCmtoIn(float cm)
	{
		return (cm * 0.393700787f);
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
