package hu.bme.aut.sensorinfo;

public class SensorPos
{
	private float x;
	private float y;
	private float z;

	public SensorPos(float _x, float _y, float _z)
	{
		x = _x;
		y = _y;
		z = _z;
	}


	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}
}
