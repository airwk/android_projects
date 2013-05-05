package airwk.graphics.shading;

public class Material
{
	private float[] baseColor = new float[]{ 0.5f, 0.5f, 0.5f };
	private Texture2D baseTexture;
	
	public Material()
	{

	}

	@Override
	public Material clone()
	{
		Material material = new Material();
		//material.shader = shader;
		material.baseColor = new float[]{ baseColor[0], baseColor[1], baseColor[2] };
		material.baseTexture = baseTexture;

		return material;
	}
	
	public float[] getBaseColor()
	{
		return baseColor;
	}

	public void setBaseColor(float r, float g, float b)
	{
		this.baseColor[0] = r;
		this.baseColor[1] = g;
		this.baseColor[2] = b;
	}

	public Texture2D getBaseTexture()
	{
		return baseTexture;
	}

	public void setBaseTexture(Texture2D baseTexture)
	{
		this.baseTexture = baseTexture;
	}
}
