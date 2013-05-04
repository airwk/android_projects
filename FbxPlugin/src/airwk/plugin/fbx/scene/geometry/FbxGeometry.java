package airwk.plugin.fbx.scene.geometry;

import java.util.ArrayList;

public class FbxGeometry extends FbxNodeAttribute
{
	private final ArrayList<FbxDeformer> deformers = new ArrayList<FbxDeformer>();
	
	protected FbxGeometry(long uniqueID, int attributeType)
	{
		super(uniqueID, attributeType);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<FbxDeformer> getDeformers()
	{
		return deformers;
	}
	
	public boolean addDeformer(FbxDeformer deformer)
	{
		return deformers.add(deformer);
	}
	
	public boolean removeDeformer(FbxDeformer deformer)
	{
		return deformers.remove(deformer);
	}
	
	public FbxDeformer getDeformer(int index)
	{
		return deformers.get(index);
	}
	
	public int getDeformerCount()
	{
		return deformers.size();
	}
}
