package plia.plugin.fbx.math;

public final class FbxVector3
{
	// Properties
	public float x, y, z;

	// Constructor
	public FbxVector3()
	{

	}

	public FbxVector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public FbxVector3(float[] vec)
	{
		this.x = vec[0];
		this.y = vec[1];
		this.z = vec[2];
	}

	public FbxVector3(FbxVector3 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		float xx = ( Math.round(x * 1000) ) / 1000f;
		float yy = ( Math.round(y * 1000) ) / 1000f;
		float zz = ( Math.round(z * 1000) ) / 1000f;

		return "X : " + xx + ", Y : " + yy + ", Z : " + zz;
	}

	@Override
	public FbxVector3 clone()
	{
		return new FbxVector3(x, y, z);
	}

	public void copyTo(float[] v)
	{
		v[0] = x;
		v[1] = y;
		v[2] = z;
	}

	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(FbxVector3 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	// Methods
	public float getMagnituded()
	{
		return (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public FbxVector3 getNormalized()
	{
		float length = getMagnituded();
		return new FbxVector3(x / length, y / length, z / length);
	}

	public FbxVector3 add(FbxVector3 vector)
	{
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;

		return this;
	}

	public FbxVector3 subtract(FbxVector3 vector)
	{
		this.x -= vector.x;
		this.y -= vector.y;
		this.z -= vector.z;

		return this;
	}

	public FbxVector3 scale(float value)
	{
		this.x *= value;
		this.y *= value;
		this.z *= value;

		return this;
	}

	// Classifier Method
	public static float dot(FbxVector3 vec1, FbxVector3 vec2)
	{
		return ((vec1.x * vec2.x) + (vec1.y * vec2.y) + (vec1.z * vec2.z));
	}

	public static FbxVector3 add(FbxVector3 vec1, FbxVector3 vec2)
	{
		FbxVector3 result = new FbxVector3();

		result.x = vec1.x + vec2.x;
		result.y = vec1.y + vec2.y;
		result.z = vec1.z + vec2.z;

		return result;
	}

	public static FbxVector3 subtract(FbxVector3 vec1, FbxVector3 vec2)
	{
		FbxVector3 result = new FbxVector3();

		result.x = vec1.x - vec2.x;
		result.y = vec1.y - vec2.y;
		result.z = vec1.z - vec2.z;

		return result;
	}

	public static FbxVector3 scale(FbxVector3 vec, float value)
	{
		FbxVector3 result = new FbxVector3();

		result.x = vec.x * value;
		result.y = vec.y * value;
		result.z = vec.z * value;

		return result;
	}

	public static FbxVector3 lerp(FbxVector3 start, FbxVector3 end, float t)
	{
		FbxVector3 result = new FbxVector3();

		// P = P0 + tv
		result.x = start.x + ((end.x - start.x) * t);
		result.y = start.y + ((end.y - start.y) * t);
		result.z = start.z + ((end.z - start.z) * t);

		return result;
	}

	public static float distance(FbxVector3 vec1, FbxVector3 vec2)
	{
		float dx = vec1.x - vec2.x;
		float dy = vec1.y - vec2.y;
		float dz = vec1.z - vec2.z;

		return (float) Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
	}

	public static FbxVector3 cross(FbxVector3 vec1, FbxVector3 vec2)
	{
		FbxVector3 result = new FbxVector3();

		result.x = (vec1.y * vec2.z) - (vec1.z * vec2.y);
		result.y = (vec1.z * vec2.x) - (vec1.x * vec2.z);
		result.z = (vec1.x * vec2.y) - (vec1.y * vec2.x);

		return result;
	}

	public static FbxVector3 reflect(FbxVector3 d, FbxVector3 n)
	{
		n = n.getNormalized();
		//r = d-2(d.n)n

		float dn = 2f * FbxVector3.dot(d, n);
		FbxVector3 _2dnn = FbxVector3.scale(n, dn);

		return FbxVector3.subtract(d, _2dnn);
	}

	//////

	public static FbxVector3 add(FbxVector3 result, FbxVector3 vec1, FbxVector3 vec2)
	{
		result.x = vec1.x + vec2.x;
		result.y = vec1.y + vec2.y;
		result.z = vec1.z + vec2.z;

		return result;
	}

	public static FbxVector3 subtract(FbxVector3 result, FbxVector3 vec1, FbxVector3 vec2)
	{
		result.x = vec1.x - vec2.x;
		result.y = vec1.y - vec2.y;
		result.z = vec1.z - vec2.z;

		return result;
	}

	public static FbxVector3 scale(FbxVector3 result, FbxVector3 vec, float value)
	{
		result.x = vec.x * value;
		result.y = vec.y * value;
		result.z = vec.z * value;

		return result;
	}

	public static FbxVector3 lerp(FbxVector3 result, FbxVector3 start, FbxVector3 end, float t)
	{
		// P = P0 + tv
		result.x = start.x + ((end.x - start.x) * t);
		result.y = start.y + ((end.y - start.y) * t);
		result.z = start.z + ((end.z - start.z) * t);

		return result;
	}

	public static FbxVector3 cross(FbxVector3 result, FbxVector3 vec1, FbxVector3 vec2)
	{
		result.x = (vec1.y * vec2.z) - (vec1.z * vec2.y);
		result.y = (vec1.z * vec2.x) - (vec1.x * vec2.z);
		result.z = (vec1.x * vec2.y) - (vec1.y * vec2.x);

		return result;
	}

	public static FbxVector3 normalize(FbxVector3 result, FbxVector3 vec)
	{
		float length = vec.getMagnituded();
		result.set(vec.x / length, vec.y / length, vec.z / length);

		return result;
	}
}