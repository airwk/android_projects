package plia.plugin.fbx.scene.geometry;

public class FbxSkeleton extends FbxNodeAttribute
{
	private final int skeletonType;

	public FbxSkeleton(long uniqueID, int skeletonType)
	{
		super(uniqueID, FbxNodeAttribute.Skeleton);
		this.skeletonType = skeletonType;
	}

	public int getSkeletonType()
	{
		return skeletonType;
	}
	
	// Skeleton Type
	public static final int Root 		= 30001;
	public static final int Limb 		= 30002;
	public static final int LimbNode 	= 30003;
	public static final int Effector 	= 30004;

}

