package plia.graphics;

import java.util.ArrayList;

public class Node
{
	private final ArrayList<Node> children = new ArrayList<Node>();
	private Node parent = null;
	
	protected Node()
	{
		
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
		return (Node) parent;
	}

	public boolean addChild(Node child)
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
	
	public boolean removeChild(Node child)
	{
		if(children.contains(child))
		{
			children.remove(child);
			child.parent = null;
			return true;
		}
		return false;
	}
	
	public int getChildCount()
	{
		return children.size();
	}
	
	public Node getChild(int index)
	{
		return children.get(index);
	}
}
