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

public class AccFragment extends Fragment
{
	private static SensorManager sensorService;
	private Sensor sensor;
	private DrawingView canvas;

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


	}

	@Override
	public void onResume()
	{
		super.onResume();
		createSpecs();
		canvas = getView().findViewById(R.id.Acc_sensorCanvas);
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
		maxRange.setText(sensor.getMaximumRange()+ " m/s^2");

		TextView resolution = getView().findViewById(R.id.sensorFragmentResolutionName);
		resolution.setText(sensor.getResolution()+ "m/s^2");

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
				sensorValue.setText("x: "+event.values[0]+ "\ny: "+ event.values[1]+ "\nz: "+event.values[2]);

				canvas.addPos(event.values[0],event.values[1],event.values[2]);
				canvas.setRange(sensor.getMaximumRange());
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
