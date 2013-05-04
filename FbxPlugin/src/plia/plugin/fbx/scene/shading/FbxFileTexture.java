package plia.plugin.fbx.scene.shading;

public class FbxFileTexture extends FbxTexture
{
	private String fileName;
	private String relativeFileName;
	private String mediaName;

	public FbxFileTexture(long uniqueID)
	{
		super(uniqueID);
		// TODO Auto-generated constructor stub
	}

	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getRelativeFileName()
	{
		return relativeFileName;
	}
	
	public void setRelativeFileName(String relativeFileName)
	{
		this.relativeFileName = relativeFileName;
	}
	
	public String getMediaName()
	{
		return mediaName;
	}
	
	public void setMediaName(String mediaName)
	{
		this.mediaName = mediaName;
	}
}
