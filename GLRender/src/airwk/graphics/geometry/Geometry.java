package airwk.graphics.geometry;


public class Geometry
{
	private int type;
	private float[] min = new float[3], max = new float[3];
	
	protected int[] buffers = new int[10];

	public Geometry(int type)
	{
		this.type = type;
	}
	
	public void destroy()
	{

	}

	public int getType()
	{
		return type;
	}

	public void setBuffer(int index, int value)
	{
		this.buffers[index] = value;
	}

	public int getBuffer(int index)
	{
		return buffers[index];
	}

	public float[] getMin()
	{
		return min;
	}

	public float[] getMax()
	{
		return max;
	}

	public void setMin(float[] min)
	{
		this.min = min;
	}

	public void setMax(float[] max)
	{
		this.max = max;
	}

	public static final int MESH			= 10001;
	public static final int SKINNED_MESH	= 10002;

	public static final int LINE			= 10003;
}
