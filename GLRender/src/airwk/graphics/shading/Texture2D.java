package airwk.graphics.shading;

import android.graphics.Color;

public class Texture2D
{
	private int textureBuffer;

	private int width;
	private int height;

	protected int[] pixels;

	private boolean enabledAlpha = false;
	
	public Texture2D(int texBuffer, int[] pixels, int width, int height)
	{
		this.textureBuffer = texBuffer;
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	public void destroy()
	{
		pixels = null;
	}

	public int getTextureBuffer()
	{
		return textureBuffer;
	}

	public void setTextureBuffer(int b)
	{
		this.textureBuffer = b;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public float[] getPixel(int x, int y)
	{
		int row = y * getHeight();
		int i = row + x;

		float r = Color.red(pixels[i]) / 255f;
		float g = Color.green(pixels[i]) / 255f;
		float b = Color.blue(pixels[i]) / 255f;
		float a = Color.alpha(pixels[i]) / 255f;

		return new float[]{ r, g, b, a };
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
