package plia.plugin.fbx.scene.geometry;

import java.util.ArrayList;

public class FbxSkin extends FbxDeformer
{
	private FbxNode node;
	private FbxGeometry geometry;

	private final ArrayList<FbxCluster> clusters = new ArrayList<FbxCluster>();
	
	public FbxSkin(long uniqueID)
	{
		super(uniqueID, FbxDeformer.Skin);
	}

	public FbxGeometry getGeometry()
	{
		return geometry;
	}
	
	public void setGeometry(FbxGeometry geometry)
	{
		this.geometry = geometry;
	}

	public FbxNode getNode()
	{
		return node;
	}

	public void setNode(FbxNode node)
	{
		this.node = node;
	}
	
	public ArrayList<FbxCluster> getClusters()
	{
		return clusters;
	}
	
	public boolean addCluster(FbxCluster cluster)
	{
		return clusters.add(cluster);
	}
	
	public boolean removeCluster(FbxCluster cluster)
	{
		return clusters.remove(cluster);
	}
	
	public FbxCluster getCluster(int index)
	{
		return clusters.get(index);
	}
}
