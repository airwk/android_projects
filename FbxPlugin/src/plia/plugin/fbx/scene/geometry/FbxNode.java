package plia.plugin.fbx.scene.geometry;

import java.util.ArrayList;

import plia.plugin.fbx.core.FbxObject;
import plia.plugin.fbx.math.FbxVector3;
import plia.plugin.fbx.scene.animation.FbxAnimCurveNode;
import plia.plugin.fbx.scene.shading.FbxSurfaceMaterial;

public class FbxNode extends FbxObject
{
	private FbxNode parent = null;
	private final ArrayList<FbxNode> children = new ArrayList<FbxNode>();
	private FbxNodeAttribute nodeAttribute = null;

	private FbxVector3 defaultLclTranslation = new FbxVector3();
	private FbxVector3 defaultLclRotation = new FbxVector3();
	private FbxVector3 defaultLclScaling = new FbxVector3(1,1,1);

	private FbxAnimCurveNode animCurveNodeT;
	private FbxAnimCurveNode animCurveNodeR;
	private FbxAnimCurveNode animCurveNodeS;

	private FbxSurfaceMaterial material;

	public FbxNode(long uniqueID)
	{
		super(uniqueID);
	}

	public boolean isRoot()
	{
		return parent == null;
	}

	public FbxNode getParent()
	{
		return parent;
	}

	public void setParent(FbxNode parent)
	{
		this.parent = parent;
	}

	public ArrayList<FbxNode> getChildren()
	{
		return children;
	}

	public boolean addChild(FbxNode child)
	{
		return children.add(child);
	}

	public boolean removeChild(FbxNode child)
	{
		return children.remove(child);
	}

	public FbxNode getChild(int index)
	{
		return children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public FbxVector3 getLclTranslation()
	{
		return defaultLclTranslation;
	}

	public FbxVector3 getLclTranslation(long frame)
	{
		if(animCurveNodeT != null)
		{
			return animCurveNodeT.getValue(frame);
		}
		return defaultLclTranslation;
	}

	public void setLclTranslation(FbxVector3 lclTranslation)
	{
		this.defaultLclTranslation = lclTranslation;
	}

	public void setLclTranslation(FbxAnimCurveNode lclTranslation)
	{
		this.animCurveNodeT = lclTranslation;
	}

	public FbxVector3 getLclRotation()
	{
		return defaultLclRotation;
	}

	public FbxVector3 getLclRotation(long frame)
	{
		if(animCurveNodeR != null)
		{
			return animCurveNodeR.getValue(frame);
		}

		return defaultLclRotation;
	}

	public void setLclRotation(FbxVector3 lclRotation)
	{
		this.defaultLclRotation = lclRotation;
	}

	public void setLclRotation(FbxAnimCurveNode lclRotation)
	{
		this.animCurveNodeR = lclRotation;
	}

	public FbxVector3 getLclScaling()
	{
		return defaultLclScaling;
	}

	public FbxVector3 getLclScaling(long frame)
	{
		if(animCurveNodeS != null)
		{
			return animCurveNodeS.getValue(frame);
		}

		return defaultLclScaling;
	}

	public void setLclScaling(FbxVector3 lclScaling)
	{
		this.defaultLclScaling = lclScaling;
	}

	public void setLclScaling(FbxAnimCurveNode lclScaling)
	{
		this.animCurveNodeS = lclScaling;
	}

	public FbxAnimCurveNode getAnimCurveNodeT()
	{
		return animCurveNodeT;
	}

	public FbxAnimCurveNode getAnimCurveNodeR()
	{
		return animCurveNodeR;
	}

	public FbxAnimCurveNode getAnimCurveNodeS()
	{
		return animCurveNodeS;
	}

	public FbxNodeAttribute getNodeAttribute()
	{
		return nodeAttribute;
	}

	public void setNodeAttribute(FbxNodeAttribute nodeAttribute)
	{
		this.nodeAttribute = nodeAttribute;
		this.nodeAttribute.addNode(this);
	}

	public FbxGeometry getGeometry()
	{
		return (FbxGeometry) this.nodeAttribute;
	}

	public FbxMesh getMesh()
	{
		return (FbxMesh) this.nodeAttribute;
	}

	public FbxSkeleton getSkeleton()
	{
		return (FbxSkeleton) this.nodeAttribute;
	}

	public FbxSurfaceMaterial getMaterial()
	{
		return material;
	}

	public void setMaterial(FbxSurfaceMaterial material)
	{
		this.material = material;
	}
}
