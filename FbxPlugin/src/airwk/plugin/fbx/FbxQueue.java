package airwk.plugin.fbx;

public class FbxQueue<T>
{
	private int index;
	private T elements[];
	
	public FbxQueue(T elements[])
	{
		this.elements = elements;
	}
	
	public boolean isEmpty()
	{
		return (index >= elements.length);
	}

	public T dequeue()
	{
		return elements[index++];
	}
}
