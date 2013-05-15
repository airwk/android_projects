package plia.main;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.*;
import plia.graphics.*;

public class Game extends Renderer
{
	private Sprite sprite;
	
	public Game(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// TODO Auto-generated method stub
		super.onSurfaceCreated(gl, config);
		
		try
		{
			Bitmap bitmap = BitmapFactory.decodeStream(getContext().getAssets().open("sticker.png"));
			Texture texture = new Texture(bitmap);
			
			sprite = new Sprite();
			sprite.setTexture(texture);
			sprite.setPosition(0, 0);
			sprite.setSize(0.5f, 0.5f);
			addChild(sprite);
			
			Sprite sprite2 = new Sprite();
			sprite2.setTexture(texture);
			sprite2.setPosition(0.5f, 0.5f);
			sprite2.setSize(0.5f, 0.5f);
			sprite.addChild(sprite2);
			
			Sprite sprite3 = new Sprite();
			sprite3.setTexture(texture);
			sprite3.setPosition(0.5f, 0.5f);
			sprite3.setSize(0.5f, 0.5f);
			sprite2.addChild(sprite3);

		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// TODO Auto-generated method stub

		super.onDrawFrame(gl);
	}
}
