package airwk.plugin.fbx.scene.geometry;

public class FbxCluster extends FbxSubDeformer
{

	private float[] weights = null;
	private int[] indices = null;
	private float[] transform = null;
	private float[] transformLink = null;
	private FbxNode associateModel;

	public FbxCluster(long uniqueID)
	{
		super(uniqueID, FbxSubDeformer.Cluster);
	}

	public FbxNode getAssociateModel()
	{
		return associateModel;
	}

	public void setAssociateModel(FbxNode associateModel)
	{
		this.associateModel = associateModel;
	}

	public float[] getTransform()
	{
		return transform;
	}

	public void setTransform(float[] transform)
	{
		this.transform = transform;
	}

	public float[] getTransformLink()
	{
		return transformLink;
	}

	public void setTransformLink(float[] transformLink)
	{
		this.transformLink = transformLink;
	}

	public int[] getIndices()
	{
		return indices;
	}

	public void setIndices(int[] indices)
	{
		this.indices = indices;
	}

	public float[] getWeights()
	{
		return weights;
	}

	public void setWeights(float[] weights)
	{
		this.weights = weights;
	}
}
