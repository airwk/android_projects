package plia.plugin.fbx.scene.geometry;

import java.util.ArrayList;

import plia.plugin.fbx.core.FbxObject;

public class FbxNodeAttribute extends FbxObject
{
	private final int attributeType;
	private final ArrayList<FbxNode> nodes = new ArrayList<FbxNode>();

	public FbxNodeAttribute(long uniqueID, int attributeType)
	{
		super(uniqueID);
		this.attributeType = attributeType;
	}

	void addNode(FbxNode node)
	{
		nodes.add(node);
	}

	public FbxNode getNode(int index)
	{
		return nodes.get(index);
	}

	public int getNodeCount()
	{
		return nodes.size();
	}

	public int getAttributeType()
	{
		return attributeType;
	}

	public static final int Mesh 		= 40001;
	public static final int Skeleton  	= 40002;
	public static final int Null 		= 40003;
}