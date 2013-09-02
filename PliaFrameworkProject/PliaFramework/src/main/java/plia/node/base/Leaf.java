package plia.node.base;

import plia.framework.BaseObject;
import plia.graphics.math.Vector2;
import plia.interfaces.IDrawable;


public abstract class Leaf extends Node
{
	private Vector2 position;
	private Vector2 size;
	
	protected Leaf()
	{
		position = new Vector2();
		size = new Vector2(1, 1);
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector2 position)
	{
		this.position = position;
	}
	
	public void setPosition(float x, float y)
	{
		this.position.set(x, y);
	}
	
	public Vector2 getSize()
	{
		return size;
	}
	
	public void setSize(Vector2 size)
	{
		this.size = size;
	}
	
	public void setSize(float x, float y)
	{
		this.size.set(x, y);
	}
}
