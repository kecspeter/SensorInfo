package hu.bme.aut.sensorinfo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LigFragment extends Fragment
{
	private static SensorManager sensorService;
	private Sensor sensor;
	private DrawingView canvas;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.lig_fragment, container, false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_LIGHT);
		if (sensor != null) {
			sensorService.registerListener(sensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		FrameLayout specs = getView().findViewById(R.id.Lig_sensorSpecs);
		View sub_specs = getLayoutInflater().inflate(R.layout.sub_sensor_specs,null);
		specs.addView(sub_specs);

		TextView ventor = getView().findViewById(R.id.sensorFragmentVentorName);
		ventor.setText(sensor.getVendor());

		TextView version = getView().findViewById(R.id.sensorFragmentVersionName);
		version.setText(sensor.getVersion()+ " ");

		TextView maxRange = getView().findViewById(R.id.sensorFragmentMaxrangeName);
		maxRange.setText(sensor.getMaximumRange()+ "lx");

		TextView resolution = getView().findViewById(R.id.sensorFragmentResolutionName);
		resolution.setText(sensor.getResolution()+ "lx");

		TextView current = getView().findViewById(R.id.sensorFragmentPowerName);
		current.setText(sensor.getPower()+ "mA");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		canvas = getView().findViewById(R.id.Lig_sensorCanvas);
		canvas.setRange(sensor.getMaximumRange()/3);
	}


	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if(event.sensor.getType() == Sensor.TYPE_LIGHT)
			{
				TextView sensorValue = getView().findViewById(R.id.sensorFragmentValue);
				sensorValue.setText(event.values[0]+" lx");

				canvas.addPos(event.values[0]);

				canvas.invalidate();
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	};

	@Override
	public void onDestroyView()
	{
		if (sensor != null) {
			sensorService.unregisterListener(sensorEventListener);
		}
		super.onDestroyView();
	}
}
