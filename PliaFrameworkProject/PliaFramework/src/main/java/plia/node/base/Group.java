package plia.node.base;

import java.util.ArrayList;

import plia.framework.BaseObject;
import plia.graphics.Canvas;


public class Group<T extends Node> extends Node
{
	private final ArrayList<T> children = new ArrayList<T>();
	
	protected Group()
	{
		// TODO Auto-generated constructor stub
	}

    @Override
    public void update(float elapsedTime)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            getChild(i).update(elapsedTime);
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            getChild(i).draw(canvas);
        }
    }

	public boolean addChild(T child)
	{
		if(!children.contains(child))
		{
			children.add(child);
			return true;
		}
		
		return false;
	}

	public boolean removeChild(T child)
	{
		if(children.contains(child))
		{
			children.remove(child);
			return true;
		}
		return false;
	}

	public int getChildCount()
	{
		return children.size();
	}

	public T getChild(int index)
	{
		return children.get(index);
	}
}
