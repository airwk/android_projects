package airwk.graphics;

import java.util.ArrayList;

import airwk.core.BaseObject;

public class Layer extends BaseObject
{
	private final ArrayList<Node> children = new ArrayList<Node>();
	
	public final int getChildCount()
	{
		return children.size();
	}
	
	public final boolean addChild(Node child)
	{
		if(!children.contains(child))
		{
			children.add(child);
			return true;
		}
		return false;
	}

	public final boolean rempoveChild(Node child)
	{
		if(children.contains(child))
		{
			children.remove(child);
			return true;
		}
		return false;
	}
}
