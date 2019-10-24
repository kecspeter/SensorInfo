package hu.bme.aut.sensorinfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View
{
	private ArrayList<SensorPos> posTable = new ArrayList<>();
	private ArrayList<Float> stateTable = new ArrayList<>();
	private float scale = 2.0f;
	private float center = 1;
	private float range = 0.0f;

	public DrawingView(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		canvas.scale(scale,scale);


		canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
		float Res = (canvas.getWidth() / range)/scale;
		//Log.println(Log.INFO, "Canvas", "Width "  + "\t"+ Res);
		if(posTable.size() > 1)
		{
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("X-axis", 0, 24, paint);
			for (int i = 0; i < posTable.size(); i++)
			{
				float pos = center - posTable.get(i).getX() * Res;
				canvas.drawPoint(i, pos, paint);
			}

			paint.setColor(Color.GREEN);
			canvas.drawText("Y-axis", 0, 48, paint);
			for (int i = 0; i < posTable.size(); i++)
			{
				float pos = center - posTable.get(i).getY() * Res;
				canvas.drawPoint(i, pos, paint);
			}

			paint.setColor(Color.BLUE);
			canvas.drawText("Z-axis", 0, 72, paint);
			for (int i = 0; i < posTable.size(); i++)
			{
				float pos = center - posTable.get(i).getZ() * Res;
				canvas.drawPoint(i, pos, paint);
			}
		}
		if(stateTable.size() > 1)
		{
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("Sensor data", 0, 24, paint);
			for (int i = 0; i < stateTable.size(); i++)
			{
				float pos = center*2-stateTable.get(i) * Res;
				canvas.drawPoint(i, pos, paint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		center = (((float)width)/2)/scale;
		setMeasuredDimension(width, width);
	}

	public void setRange(float _range)
	{
		range = _range;
	}

	public void addPos(float x, float y, float z)
	{
		if(posTable.size() > (getMeasuredWidth()/scale))
		{
			posTable.remove(0);
		}
		posTable.add(new SensorPos(x,y,z));
	}
	public void addPos(float x)
	{
		if(stateTable.size() > (getMeasuredWidth()/scale))
		{
			stateTable.remove(0);
		}
		stateTable.add(x);
	}
}
