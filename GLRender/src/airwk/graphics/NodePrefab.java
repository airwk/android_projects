package airwk.graphics;

import airwk.graphics.geometry.Mesh;
import airwk.graphics.shading.Material;

public class NodePrefab
{
	private Mesh mesh;
	private Material material;
	private boolean hasAnimation;
	
	public Mesh getMesh()
	{
		return mesh;
	}

	public void setMesh(Mesh mesh)
	{
		this.mesh = mesh;
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public boolean hasAnimation()
	{
		return hasAnimation;
	}

	public void setHasAnimation(boolean hasAnimation)
	{
		this.hasAnimation = hasAnimation;
	}

	public void resume()
	{
		mesh.resume();
	}

	public void destroy()
	{
		mesh.destroy();
		mesh = null;
		material = null;
	}
}
