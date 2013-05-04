package airwk.plugin.fbx.scene.geometry;

import airwk.plugin.fbx.core.FbxObject;

public class FbxDeformer extends FbxObject
{
	private final int deformerType;

	public FbxDeformer(long uniqueID, int deformerType)
	{
		super(uniqueID);
		this.deformerType = deformerType;
	}

	public int getDeformerType()
	{
		return deformerType;
	}
	
	public static final int Unidentified 	= 20001;
	public static final int Skin 		 	= 20002;
	public static final int VertexCache 	= 20003;
	public static final int DeformerCount 	= 20004;
}