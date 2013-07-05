package game.scene;

import game.manager.SceneManager.SceneType;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

public abstract class BaseScene extends Scene
{
	public BaseScene()
	{
		// TODO Auto-generated constructor stub
		this.onCreated();
	}

	public abstract void onCreated();
	public abstract void onDisposed();
	public abstract SceneType getSceneType();
	
	public void appearAnimation()
	{
		this.setScaleCenter(640, 360);
		this.setScale(0);
		this.registerUpdateHandler(new IUpdateHandler()
		{
			
			@Override
			public void reset()
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				// TODO Auto-generated method stub
				BaseScene bs = BaseScene.this;
				
				float sc = bs.getScaleX()+0.1f;
				bs.setScale(sc);
				
				if(sc > 0.9f)
				{
					bs.setScale(1);
					bs.unregisterUpdateHandler(this);
				}
			}
		});
	}
}
