package airwk.graphics;

import java.util.ArrayList;

import airwk.core.BaseObject;

public class Scene extends BaseObject
{
	private final ArrayList<Layer> children = new ArrayList<Layer>();
	
	public final int getChildCount()
	{
		return children.size();
	}
	
	public final boolean addChild(Layer child)
	{
		if(!children.contains(child))
		{
			children.add(child);
			return true;
		}
		return false;
	}

	public final boolean rempoveChild(Layer child)
	{
		if(children.contains(child))
		{
			children.remove(child);
			return true;
		}
		return false;
	}
}
