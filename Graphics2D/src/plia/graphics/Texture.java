package plia.graphics;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;
import android.graphics.Bitmap;

public class Texture
{
	private int textureBuffer;

	private Bitmap bitmap;

	private boolean enabledAlpha = false;

	public Texture(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		
		int[] tex = new int[1];

		glGenTextures(1, tex, 0);

		// generate color texture
		glBindTexture(GL_TEXTURE_2D, tex[0]);

		// parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		glGenerateMipmap(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, 0);
		
		textureBuffer = tex[0];
	}
	
	public int getTextureBuffer()
	{
		return textureBuffer;
	}
	
	public int getWidth()
	{
		return bitmap.getWidth();
	}

	public int getHeight()
	{
		return bitmap.getHeight();
	}

	public int getPixel(int x, int y)
	{
		return bitmap.getPixel(x, y);
	}

	public void setEnabledAlpha(boolean enabledAlpha)
	{
		this.enabledAlpha = enabledAlpha;
	}

	public boolean isEnabledAlpha()
	{
		return enabledAlpha;
	}
	
}