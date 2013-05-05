package airwk.graphics;

import airwk.graphics.animation.Animation;
import airwk.graphics.shading.Material;

public class ScenePrefab
{
	private float[] axisRotation;

	private Animation animation;

	private NodePrefab[] nodePrefabs;
	private Material[] materials;

	private Material defaultMaterial = new Material();
	
	public float[] getAxisRotation()
	{
		return axisRotation;
	}

	public Animation getAnimation()
	{
		return animation;
	}

	public NodePrefab[] getNodePrefabs()
	{
		return nodePrefabs;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}

	public void setAxisRotation(float[] axisRotation)
	{
		this.axisRotation = axisRotation;
	}

	public void setNodePrefabs(NodePrefab[] nodePrefabs)
	{
		this.nodePrefabs = nodePrefabs;
	}

	public Material[] getMaterials()
	{
		return materials;
	}

	public void setMaterials(Material[] materials)
	{
		this.materials = materials;
	}

	public Material getDefaultMaterial()
	{
		return defaultMaterial;
	}

	public void resume()
	{
		for (int i = 0; i < nodePrefabs.length; i++)
		{
			nodePrefabs[i].resume();
		}
	}

	public void destroy()
	{
		axisRotation = null;

		animation = null;

		for (NodePrefab nodePrefab : nodePrefabs)
		{
			nodePrefab.destroy();
		}

		nodePrefabs = null;
		materials = null;

		defaultMaterial = null;
	}
}
