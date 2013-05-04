package plia.plugin.fbx.core;

public class FbxObject
{
	private String name;
	private final long uniqueID;

	public FbxObject(long uniqueID)
	{
		this.uniqueID = uniqueID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getUniqueID()
	{
		return uniqueID;
	}


	//	// SubDeformerType
	//	public static final int Cluster 		= 10001;
	//
	//	// Deformer Type
	//	public static final int Unidentified 	= 20001;
	//	public static final int Skin 			= 20002;
	//	public static final int VertexCache 	= 20003;
	//	public static final int DeformerCount 	= 20004;
	//
	//	// Skeleton Type
	//	public static final int Root 			= 30001;
	//	public static final int Limb 			= 30002;
	//	public static final int LimbNode 		= 30003;
	//	public static final int Effector 		= 30004;
	//
	//	// Node Attribute Type
	//	public static final int Mesh 			= 40001;
	//	public static final int Skeleton 		= 40002;
	//	public static final int Null 			= 40003;
}