package plia.plugin.fbx.scene.animation;

import plia.plugin.fbx.core.FbxObject;

public class FbxAnimCurve extends FbxObject
{
	private long[] keyTime;
	private float[] keyValue;
	private float[] tangent;

	public FbxAnimCurve(long uniqueID)
	{
		super(uniqueID);
	}
	
	public void set(long[] times, float[] values)
	{
		keyTime = times;
		keyValue = values;
		
		int size = values.length;
		
		tangent = new float[size];
		
		if(size > 2)
		{
			for (int i = 0; i < size; i++)
			{
				if(i > 1)
				{
					float t2 = values[i];
					float t1 = values[i-2];
					
					float t = (t2-t1) * 0.5f;
					
					tangent[i-1] = t;
				}
				else if (i == 1 || i == size-1)
				{
					tangent[i] = 0;
				}
			}
		}
	}
	
	public float getValue(long frame)
	{
		float v = 0;
		int size = keyTime.length;
		
		if(size == 0)
		{
			v = 0;
		}
		else
		{
			long time = (long) (1539538600L * frame);
	
			
			
			int test = -1;
			
			for (int i = 0; i < size; i++)
			{
				if(keyTime[i] == time)
				{
					test = i;
					break;
				}
			}
			
			if(test == -1 && time < keyTime[0])
			{
				v = keyValue[0];
			}
			else if(test > -1)
			{
				v = keyValue[test];
			}
			else
			{
				int ids = -1;
				int	idt = -1;
		
				for (int i = 0; i < size; i++)
				{
					if(time < keyTime[i])
					{
						ids = i-1;
						idt = i;
//						Log.e(frame+" : "+time, ids+", "+idt);
						break;
					}
				}
				
				if(ids+idt == -2)
				{
					v = keyValue[size-1];
					
				}
				else
				{
					
					long start = keyTime[ids];
					long end = keyTime[idt];
					
					long s1 = time - start;
					long s2 = end - start;
					
					float t = (float)s1 / s2;
					
					float v1 = keyValue[ids];
					float v2 = keyValue[idt];
					
					if(s2 == 1539538600L || size < 3)
					{
						// Linear Interpolate
						v = v1 + (t * (v2 - v1));
						
					}
					else
					{
						// Hermite Curve Interpolate
						float t2 = t*t;
						float t3 = t2*t;
						
						float _2t3 = 2.0f*t3;
						float _3t2 = 3.0f*t2;
						
						float h1 = _2t3 - _3t2 + 1.0f;
						float h2 = -_2t3 + _3t2;
						float h3 = t3 - (2*t2) + t;
						float h4 = t3 - t2;
						
						// Find Tangent
						float tg1 = tangent[ids];
						float tg2 = tangent[idt];
						
						v = (h1 * v1) + (h2 * v2) + (h3 * tg1) + (h4 * tg2);
						
					}
				}
			}
		}
		
		
		return v;
	}
	
	public int getStartFrame()
	{
		if(keyTime != null && keyTime.length > 0)
		{
			return (int) (keyTime[0] / 1539538600L);
		}
		
		return 0;
	}
	
	public int getStopFrame()
	{
		if(keyTime != null && keyTime.length > 0)
		{
			return (int) (keyTime[keyTime.length-1] / 1539538600L);
		}
		
		return 0;
	}
	
	public int getTotalFrame()
	{
		if(keyTime != null && keyTime.length > 0)
		{
			return (int) ((keyTime[keyTime.length-1] - keyTime[0]) / 1539538600L) + 1;
		}
		
		return 0;
	}
}
