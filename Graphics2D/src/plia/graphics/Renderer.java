package plia.graphics;

import static android.opengl.GLES20.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;

public class Renderer extends Node implements GLSurfaceView.Renderer
{
	private Context context;
	private Canvas canvas;
	private int width, height;
	private float screenRed, screenGreen, screenBlue;
	
	
	public Renderer(Context context)
	{
		// TODO Auto-generated constructor stub
		this.context = context;
		this.setScreenColor(Color.rgb(100, 149, 237));
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// TODO Auto-generated method stub
		this.canvas = new Canvas();
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		// TODO Auto-generated method stub
		this.width = width;
		this.height = height;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		// TODO Auto-generated method stub
		glViewport(0, 0, getWidth(), getHeight());
		glClearColor( screenRed , screenGreen, screenBlue, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glDisable(GL_DEPTH_TEST);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		for (int i = 0; i < super.getChildCount(); i++)
		{
			Node child = super.getChild(i);
			if(child instanceof Sprite)
			{
				((Sprite) child).draw(canvas);
			}
		}
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getScreenColor()
	{
		return Color.rgb((int)(screenRed * 255), (int)(screenGreen * 255), (int)(screenBlue * 255));
	}
	public void setScreenColor(int screenColor)
	{
		this.screenRed = Color.red(screenColor) / 255f;
		this.screenGreen = Color.green(screenColor) / 255f;
		this.screenBlue = Color.blue(screenColor) / 255f;
	}
}

