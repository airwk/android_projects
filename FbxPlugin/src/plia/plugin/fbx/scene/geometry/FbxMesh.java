package plia.plugin.fbx.scene.geometry;

public class FbxMesh extends FbxGeometry
{
	private float[] vertices = null;
	private float[] normals = null;
	private float[] uv = null;
	private int[] indices = null;
	private int[] uvIndices = null;

	public FbxMesh(long uniqueID)
	{
		super(uniqueID, FbxNodeAttribute.Mesh);
	}

	public float[] getVertices()
	{
		return vertices;
	}

	public void setVertices(float[] vertices)
	{
		this.vertices = vertices;
	}

	public float[] getNormals()
	{
		return normals;
	}

	public void setNormals(float[] normals)
	{
		this.normals = normals;
	}
	
	public float[] getUV()
	{
		return uv;
	}
	
	public void setUV(float[] uv)
	{
		this.uv = uv;
	}

	public int[] getIndices()
	{
		return indices;
	}

	public void setIndices(int[] indices)
	{
		this.indices = indices;
	}
	
	public int[] getUVIndices()
	{
		return uvIndices;
	}
	
	public void setUVIndices(int[] uvIndices)
	{
		this.uvIndices = uvIndices;
	}
}
