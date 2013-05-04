package airwk.plugin.fbx.scene.shading;

import airwk.plugin.fbx.math.FbxVector3;


public class FbxSurfacePhong extends FbxSurfaceLambert
{
	private FbxVector3 specular = new FbxVector3();
	private FbxVector3 reflection = new FbxVector3();

	private float specularFactor = 0;
	private float shinniness = 0;
	private float reflectionFactor = 0;

	public FbxSurfacePhong(long uniqueID)
	{
		super(uniqueID);
		
	}

	public FbxVector3 getSpecular()
	{
		return specular;
	}
	
	public FbxVector3 getReflection()
	{
		return reflection;
	}
	
	public float getSpecularFactor()
	{
		return specularFactor;
	}
	
	public float getShinniness()
	{
		return shinniness;
	}
	
	public float getReflectionFactor()
	{
		return reflectionFactor;
	}
	
	public void setSpecular(float r, float g, float b)
	{
		this.specular.x = r;
		this.specular.y = g;
		this.specular.z = b;
	}
	
	public void setReflection(float r, float g, float b)
	{
		this.reflection.x = r;
		this.reflection.y = g;
		this.reflection.z = b;
	}
	
	public void setSpecularFactor(float specularFactor)
	{
		this.specularFactor = specularFactor;
	}
	
	public void setShinniness(float shinniness)
	{
		this.shinniness = shinniness;
	}
	
	public void setReflectionFactor(float reflectionFactor)
	{
		this.reflectionFactor = reflectionFactor;
	}
}
