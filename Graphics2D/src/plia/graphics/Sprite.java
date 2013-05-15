package plia.graphics;

import android.graphics.PointF;
import android.opengl.Matrix;

public class Sprite extends Node
{
	private Texture texture;
	private PointF position;
	private PointF size;
	
	private float[] world;
	private boolean hasChanged;
	
	public Sprite()
	{
		// TODO Auto-generated constructor stub
		this.position = new PointF();
		this.size = new PointF(1, 1);

		this.world = new float[]{ 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1 };
		
		this.setChanged();
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public float getWidth()
	{
		return size.x;
	}
	
	public float getHeight()
	{
		return size.y;
	}
	
	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
	
	public void setPosition(float x, float y)
	{
		this.position.set(x, y);
		this.setChanged();
	}
	
	public void setSize(float width, float height)
	{
		this.size.set(width, height);
		this.setChanged();
	}
	
	public final void draw(Canvas canvas)
	{
		this.drawHierarchy(canvas, null, false);
	}
	
	private void drawHierarchy(Canvas canvas, float[] parentWorld, boolean parentHasChanged)
	{
		if(hasChanged || parentHasChanged)
		{
			float[] local = { size.x,0,0,0, 0,size.y,0,0, 0,0,1,0, position.x,position.y,0,1 };
			
			if(parentWorld == null)
			{
				world = local;
			}
			else
			{
				Matrix.multiplyMM(world, 0, parentWorld, 0, local, 0);
			}
			
			hasChanged = false;
			parentHasChanged = true;
		}
		
		this.onDraw(canvas);
		
		for (int i = 0; i < super.getChildCount(); i++)
		{
			Node child = super.getChild(i);
			if(child instanceof Sprite)
			{
				((Sprite) child).drawHierarchy(canvas, world, parentHasChanged);
			}
		}
	}
	
	protected void onDraw(Canvas canvas)
	{
		if(texture != null)
		{
			canvas.drawSprite(texture, world, null);
		}
	}

	public void setChanged()
	{
		this.hasChanged = true;
	}

}
