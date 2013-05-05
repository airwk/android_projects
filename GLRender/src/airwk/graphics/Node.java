package airwk.graphics;

import java.util.ArrayList;

import airwk.core.BaseObject;
import airwk.graphics.animation.Animation;
import airwk.graphics.shading.Material;
import android.opengl.Matrix;

public class Node extends BaseObject
{
	private final ArrayList<Node> children = new ArrayList<Node>();
	private Node parent = null;
	
	private float[] localScaling = null;
	private float[] axisRotation = null;
	
	private float[] world = null;
	private float[] invParentWorld = null;
	
	private boolean hasChanged = true;
	
	private boolean hasAnimation = false;
	private Animation animation = null;
	
	private Material material = null;
	
	private Camera camera = null;
	
	public Node()
	{
		localScaling = new float[3];
		axisRotation = new float[16];
		
		world = new float[16];
		invParentWorld = new float[16];
		
		Matrix.setIdentityM(axisRotation, 0);
		Matrix.setIdentityM(world, 0);
		Matrix.setIdentityM(invParentWorld, 0);
	}
	
	public void render()
	{
		
	}

	public void update()
	{
		updateHierarchy(false);
	}
	
	public void updateHierarchy(boolean parentHasChanged)
	{
		if(hasChanged || parentHasChanged)
		{
			if(parent != null)
			{
				float[] local = new float[16];
				Matrix.multiplyMM(local, 0, invParentWorld, 0, world, 0);
				Matrix.multiplyMM(world, 0, parent.world, 0, local, 0);
				Matrix.invertM(invParentWorld, 0, parent.world, 0);
			}
			
			hasChanged = false;
			parentHasChanged = true;
		}
		
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).updateHierarchy(parentHasChanged);
		}
	}
	
	public final boolean isRoot()
	{
		return parent == null;
	}

	public final Node getRoot()
	{
		return findRoot(this);
	}

	private Node findRoot(Node node)
	{
		Node parent = (Node) node.parent;

		if(parent != null)
		{
			return findRoot(parent);
		}

		return node;
	}

	public final Node getParent()
	{
		return parent;
	}
	
	public final int getChildCount()
	{
		return children.size();
	}
	
	public final boolean addChild(Node child)
	{
		if(child.parent == null)
		{
			if(!children.contains(child))
			{
				children.add(child);
				child.parent = this;
				return true;
			}
		}
		return false;
	}

	public final boolean rempoveChild(Node child)
	{
		if(children.contains(child))
		{
			children.remove(child);
			child.parent = null;
			return true;
		}
		return false;
	}

	///
	
	public float[] getAxisRotation()
	{
		return axisRotation;
	}

	public void setAxisRotation(float[] axisRotation)
	{
		this.axisRotation = axisRotation;
	}

	public float[] getWorldMatrix()
	{
		return world;
	}

	public float[] getPosition()
	{
		return new float[] { world[12], world[13], world[14] };
	}

	public float[] getRotation()
	{
		return new float[] { world[0], world[1], world[2], world[4], world[5], world[6], world[8], world[9], world[10] };
	}

	public float[] getScale()
	{
		return new float[] { localScaling[0], localScaling[1], localScaling[2] };
	}
	
	public float[] getEulerAngles()
	{
		float heading, attitude, bank;
		float m11 = world[0];
		float m12 = world[1];
		float m13 = world[2];
		float m22 = world[5];
		float m31 = world[8];
		float m32 = world[9];

		if (m12 > 0.998f) { // singularity at north pole
			heading = (float) Math.atan2(m31,m22);
			attitude = (float) (Math.PI/2);
			bank = 0;

		}
		else if (m12 < -0.998f) { // singularity at south pole
			heading = (float) Math.atan2(m31,m22);
			attitude = (float) (-Math.PI/2);
			bank = 0;
		}
		else
		{
			heading = (float) Math.atan2(-m13,m11);
			bank = (float) Math.atan2(-m32,m22);
			attitude = (float) Math.asin(m12);
		}

		float x = (float) Math.toDegrees(bank);
		float y = (float) Math.toDegrees(heading);
		float z = (float) Math.toDegrees(attitude);
		
		return new float[] { x, y, z };
	}

	public float[] getRight()
	{
		return new float[] { world[0], world[1], world[2] };
	}

	public float[] getForward()
	{
		return new float[] { world[4], world[5], world[6] };
	}

	public float[] getUp()
	{
		return new float[] { world[8], world[9], world[10] };
	}
	
	
	
	public void setPosition(float[] position)
	{
		world[12] = position[0];
		world[13] = position[1];
		world[14] = position[2];
		this.hasChanged = true;
	}
	public void setPosition(float x, float y, float z)
	{
		world[12] = x;
		world[13] = y;
		world[14] = z;
		this.hasChanged = true;
	}
	public void setScale(float[] scale)
	{
		localScaling[0] = scale[0];
		localScaling[1] = scale[1];
		localScaling[2] = scale[2];
		this.hasChanged = true;
	}

	public void setScale(float x, float y, float z)
	{
		localScaling[0] = x;
		localScaling[1] = y;
		localScaling[2] = z;
		this.hasChanged = true;
	}

	public void setEulerAngles(float[] eulerAngles)
	{
		setEulerAngles(eulerAngles[0], eulerAngles[1], eulerAngles[2]);
	}

	public void setEulerAngles(float x, float y, float z)
	{
		// Degreen to Radian
		float rx = x * 0.0174533f;
		float ry = y * 0.0174533f;
		float rz = z * 0.0174533f;
				
		// Heading ( Yaw :: Y Axis ) ; First
		float ch = (float) Math.cos(ry);
		float sh = (float) Math.sin(ry);
					    
		// Attitude ( Pitch :: Z Axis ) ; Second
		float ca = (float) Math.cos(rz);
		float sa = (float) Math.sin(rz);
				    
		// Bank ( Roll :: X Axis ) ; Last
		float cb = (float) Math.cos(rx);
		float sb = (float) Math.sin(rx);
			    
		world[0] = ch*ca;
		world[1] = sa;
		world[2] = -sh*ca;
			    
		world[4] = sh*sb - ch*sa*cb;
		world[5] = ca*cb;
		world[6] = sh*sa*cb + ch*sb;

		world[8] = ch*sa*sb + sh*cb;
		world[9] = -ca*sb;
		world[10] = -sh*sa*sb + ch*cb;
		
		this.hasChanged = true;
	}

	public void setRight(float[] right)
	{
		world[0] = right[0];
		world[1] = right[1];
		world[2] = right[2];
		this.hasChanged = true;
	}

	public void setRight(float x, float y, float z)
	{
		world[0] = x;
		world[1] = y;
		world[2] = z;
		this.hasChanged = true;
	}

	public void setForward(float[] forward)
	{
		world[4] = forward[0];
		world[5] = forward[1];
		world[6] = forward[2];
		this.hasChanged = true;
	}

	public void setForward(float x, float y, float z)
	{
		world[4] = x;
		world[5] = y;
		world[6] = z;
		this.hasChanged = true;
	}

	public void setUp(float[] up)
	{
		world[8] = up[0];
		world[9] = up[1];
		world[10] = up[2];
		this.hasChanged = true;
	}

	public void setUp(float x, float y, float z)
	{
		world[8] = x;
		world[9] = y;
		world[10] = z;
		this.hasChanged = true;
	}
	
	public void setLookAt(float[] target)
	{
		setLookAt(target[0], target[1], target[2]);
	}
	
	public void setLookAt(float targetX, float targetY, float targetZ)
	{
		float fx = targetX - world[12];
		float fy = targetY - world[13];
		float fz = targetZ - world[14];

		float magnitude = (float) Math.sqrt((fx * fx) + (fy * fy) + (fz * fz));
		float nx = fx / magnitude;
		float ny = fy / magnitude;
		float nz = fy / magnitude;

		setForward(nx, ny, nz);
	}

	public void setLookAt(Node target)
	{
		setLookAt(target.getPosition());
	}
	
	public void translate(float x, float y, float z)
	{
		if(Float.isNaN(x))
		{
			x = 0;
		}
		if(Float.isNaN(y))
		{
			y = 0;
		}
		if(Float.isNaN(z))
		{
			z = 0;
		}

		world[12] += (x * world[0]) + (y * world[4]) + (z * world[8]);
		world[13] += (x * world[1]) + (y * world[5]) + (z * world[9]);
		world[14] += (x * world[2]) + (y * world[6]) + (z * world[10]);


		this.hasChanged = true;
	}
	
	public void translate(float x, float y, float z, boolean relativeWorld)
	{
		if(relativeWorld)
		{
			if(Float.isNaN(x))
			{
				x = 0;
			}
			if(Float.isNaN(y))
			{
				y = 0;
			}
			if(Float.isNaN(z))
			{
				z = 0;
			}
			
			world[12] += x;
			world[13] += y;
			world[14] += z;
			
			this.hasChanged = true;
		}
		else
		{
			translate(x, y, z);
		}

	}
	
	public void rotate(float x, float y, float z)
	{
		if(Float.isNaN(x))
		{
			x = 0;
		}
		if(Float.isNaN(y))
		{
			y = 0;
		}
		if(Float.isNaN(z))
		{
			z = 0;
		}
		
		// Degreen to Radian
		float rx = x * 0.0174533f;
		float ry = y * 0.0174533f;
		float rz = z * 0.0174533f;
				
		// Heading ( Yaw :: Y Axis ) ; First
		float ch = (float) Math.cos(ry);
		float sh = (float) Math.sin(ry);
					    
		// Attitude ( Pitch :: Z Axis ) ; Second
		float ca = (float) Math.cos(rz);
		float sa = (float) Math.sin(rz);
				    
		// Bank ( Roll :: X Axis ) ; Last
		float cb = (float) Math.cos(rx);
		float sb = (float) Math.sin(rx);
		
		float[] rot = new float[9];
		
		rot[0] = ch*ca;
		rot[1] = sa;
		rot[2] = -sh*ca;
			    
		rot[3] = sh*sb - ch*sa*cb;
		rot[4] = ca*cb;
		rot[5] = sh*sa*cb + ch*sb;

		rot[6] = ch*sa*sb + sh*cb;
		rot[7] = -ca*sb;
		rot[8] = -sh*sa*sb + ch*cb;
		
		//
		float m11 = (world[0]*rot[0]) + (world[4]*rot[1]) + (world[8]*rot[2]);
		float m12 = (world[1]*rot[0]) + (world[5]*rot[1]) + (world[9]*rot[2]);
		float m13 = (world[2]*rot[0]) + (world[6]*rot[1]) + (world[10]*rot[2]);

		float m21 = (world[0]*rot[3]) + (world[4]*rot[4]) + (world[8]*rot[5]);
		float m22 = (world[1]*rot[3]) + (world[5]*rot[4]) + (world[9]*rot[5]);
		float m23 = (world[2]*rot[3]) + (world[6]*rot[4]) + (world[10]*rot[5]);

		float m31 = (world[0]*rot[6]) + (world[4]*rot[7]) + (world[8]*rot[8]);
		float m32 = (world[1]*rot[6]) + (world[5]*rot[7]) + (world[9]*rot[8]);
		float m33 = (world[2]*rot[6]) + (world[6]*rot[7]) + (world[10]*rot[8]);

		world[0] = m11;
		world[1] = m12;
		world[2] = m13;
		
		world[4] = m21;
		world[5] = m22;
		world[6] = m23;
		
		world[8] = m31;
		world[9] = m32;
		world[10] = m33;
		
		this.hasChanged = true;
	}

	public void rotate(float x, float y, float z, boolean relativeWorld)
	{
		if(relativeWorld)
		{
			if(Float.isNaN(x))
			{
				x = 0;
			}
			if(Float.isNaN(y))
			{
				y = 0;
			}
			if(Float.isNaN(z))
			{
				z = 0;
			}
			
			// Degreen to Radian
			float rx = x * 0.0174533f;
			float ry = y * 0.0174533f;
			float rz = z * 0.0174533f;
			
			// Heading ( Yaw :: Y Axis ) ; First
			float ch = (float) Math.cos(ry);
			float sh = (float) Math.sin(ry);
				    
			// Attitude ( Pitch :: Z Axis ) ; Second
			float ca = (float) Math.cos(rz);
			float sa = (float) Math.sin(rz);
			    
			// Bank ( Roll :: X Axis ) ; Last
			float cb = (float) Math.cos(rx);
			float sb = (float) Math.sin(rx);
			
			float[] rot = new float[16];
			
			rot[0] = ch*ca;
			rot[1] = sa;
			rot[2] = -sh*ca;
			rot[3] = 0;
				    
			rot[4] = sh*sb - ch*sa*cb;
			rot[5] = ca*cb;
			rot[6] = sh*sa*cb + ch*sb;
			rot[7] = 0;

			rot[8] = ch*sa*sb + sh*cb;
			rot[9] = -ca*sb;
			rot[10] = -sh*sa*sb + ch*cb;
			rot[11] = 0;
			
			rot[12] = 0;
			rot[13] = 0;
			rot[14] = 0;
			rot[15] = 1;
			
			float[] m = new float[16];
			Matrix.multiplyMM(m, 0, rot, 0, world, 0);
			
			for (int i = 0; i < m.length; i++)
			{
				world[i] = m[i];
			}

			this.hasChanged = true;
		}
		else
		{
			rotate(x, y, z);
		}

	}
	//
	
	public boolean hasAnimation()
	{
		return hasAnimation;
	}

	public Animation getAnimation()
	{
		return animation;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
		this.hasAnimation = (animation != null);
	}
	//
	
	public Material getMaterial()
	{
		return material;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	//
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public void setCamera(Camera camera)
	{
		if(camera != null)
		{
			if(camera.node != null)
			{
				camera.node.setCamera(null);
			}
			
			camera.node = this;
		}
		this.camera = camera;
	}
	
	//
}
