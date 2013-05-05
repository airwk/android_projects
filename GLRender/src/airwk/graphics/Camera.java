package airwk.graphics;

public final class Camera
{
	private float range;
	private ProjectionType projectionType = ProjectionType.PERSPECTIVE;
	protected Node node;
	
	public Camera()
	{
		
	}
	
	@Override
	public Camera clone()
	{
		Camera c = new Camera();
		c.range = range;
		c.projectionType = projectionType;
		
		return c;
	}
	
	public Node getNode()
	{
		return node;
	}

	public float getRange()
	{
		return range;
	}

	public void setRange(float range)
	{
		this.range = range;
	}

	public ProjectionType getProjectionType()
	{
		return projectionType;
	}

	public void setProjectionType(ProjectionType projectionType)
	{
		this.projectionType = projectionType;
	}
	
}
