package airwk.plugin.fbx.scene;

import java.util.ArrayList;

import airwk.plugin.fbx.core.FbxGlobalSetting;
import airwk.plugin.fbx.scene.geometry.FbxGeometry;
import airwk.plugin.fbx.scene.geometry.FbxNode;
import airwk.plugin.fbx.scene.shading.FbxSurfaceMaterial;


public class FbxScene
{
	private FbxNode rootnodes = new FbxNode(0);
	private ArrayList<FbxNode> nodes = new ArrayList<FbxNode>();
	private ArrayList<FbxGeometry> geometries = new ArrayList<FbxGeometry>();
	private ArrayList<FbxSurfaceMaterial> materials = new ArrayList<FbxSurfaceMaterial>();
	private FbxGlobalSetting globalSetting = new FbxGlobalSetting();

	private int startKeyframe;
	private int stopKeyframe;
	private int totalKeyframe;

	public FbxNode getRootnodes()
	{
		return rootnodes;
	}
	
	public FbxGlobalSetting getGlobalSetting()
	{
		return globalSetting;
	}
	
	public void addGeometry(FbxGeometry geometry)
	{
		geometries.add(geometry);
	}
	
	public void removeGeometry(FbxGeometry geometry)
	{
		geometries.remove(geometry);
	}
	
	public FbxGeometry getGeometry(int index)
	{
		return geometries.get(index);
	}
	
	public int getGeometryCount()
	{
		return geometries.size();
	}
	
	public void addNode(FbxNode node)
	{
		nodes.add(node);
	}
	
	public void removeNode(FbxNode node)
	{
		nodes.remove(node);
	}
	
	public FbxNode getNode(int index)
	{
		return nodes.get(index);
	}
	
	public int getNodeCount()
	{
		return nodes.size();
	}
	
	public void addMaterial(FbxSurfaceMaterial material)
	{
		materials.add(material);
	}
	
	public void removeMaterial(FbxSurfaceMaterial material)
	{
		materials.remove(material);
	}
	
	public int getMaterialCount()
	{
		return materials.size();
	}
	
	public FbxSurfaceMaterial getMaterial(int index)
	{
		return materials.get(index);
	}
	
	public void setKeyframe(int start, int stop, int total)
	{
		this.startKeyframe = start;
		this.stopKeyframe = stop;
		this.totalKeyframe = total;
	}
	
	public int getStartKeyframe()
	{
		return startKeyframe;
	}
	
	public int getStopKeyframe()
	{
		return stopKeyframe;
	}
	
	public int getTotalKeyframe()
	{
		return totalKeyframe;
	}
}
