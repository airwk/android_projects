package game.scene;

import game.core.Resources;
import game.manager.ResourcesManager;
import game.manager.SceneManager.SceneType;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class MainMenu extends BaseScene
{
	private Sprite bg, logo;
	private AnimatedSprite startBtn, creditBtn, exitBtn;
	
	private float sin;
	private IUpdateHandler updateHandler;

	@Override
	public void onCreated()
	{
		// TODO Auto-generated method stub
		ResourcesManager resourcesManager = ResourcesManager.getInstance();
		VertexBufferObjectManager vboManager = resourcesManager.getVertexBufferObjectManager();
		
		ITextureRegion bgTexture = resourcesManager.getTexture(Resources.MAINMENU_BACKGROUND);
		ITextureRegion logoTexture = resourcesManager.getTexture(Resources.MAINMENU_LOGO);
		ITiledTextureRegion startBtnTexture = resourcesManager.getTileTexture(Resources.MAINMENU_START_BUTTON);
		ITiledTextureRegion creditBtnTexture = resourcesManager.getTileTexture(Resources.MAINMENU_CREDIT_BUTTON);
		ITiledTextureRegion exittBtnTexture = resourcesManager.getTileTexture(Resources.MAINMENU_EXIT_BUTTON);
		
		bg = new Sprite(0, 0, bgTexture, vboManager);
		logo = new Sprite(150, 30, logoTexture, vboManager);
		logo.setSkewCenterY(262);
		
		startBtn = new AnimatedSprite(0, 0, startBtnTexture, vboManager)
		{
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float x, float y)
			{
				// TODO Auto-generated method stub
				if(touchEvent.isActionDown())
				{
					startBtn.setCurrentTileIndex(1);
				}
				else if(touchEvent.isActionUp())
				{
					
				}
					
				return true;
			}
		};
		startBtn.setPosition(460, 288);
		startBtn.setSkewCenterY(159);
		
		creditBtn = new AnimatedSprite(0, 0, creditBtnTexture, vboManager)
		{
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float x, float y)
			{
				// TODO Auto-generated method stub
				if(touchEvent.isActionDown())
				{
					creditBtn.setCurrentTileIndex(1);
				}
				else if(touchEvent.isActionUp())
				{
					
				}

				return true;
			}
		};
		creditBtn.setPosition(460, 453);
		creditBtn.setSkewCenterY(159);

		exitBtn = new AnimatedSprite(0, 0, exittBtnTexture, vboManager)
		{
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float x, float y)
			{
				// TODO Auto-generated method stub
				if(touchEvent.isActionDown())
				{
					exitBtn.setCurrentTileIndex(1);
				}
				else if(touchEvent.isActionUp())
				{
					ResourcesManager.getInstance().getActivity().finish();
				}
				
				return true;
			}
		};
		exitBtn.setPosition(32, 558);
		exitBtn.setSkewCenterY(159);
		
		updateHandler = new IUpdateHandler()
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
				float s = (float)Math.sin(sin)*1.5f;
				
				logo.setSkewX(s);
				startBtn.setSkewX(-s);
				creditBtn.setSkewX(-s);
				
				sin+=0.05f;
				
			}
		};
		
		attachChild(bg);
		attachChild(logo);
		attachChild(startBtn);
		attachChild(creditBtn);
		attachChild(exitBtn);
		
		registerTouchArea(startBtn);
		registerTouchArea(creditBtn);
		registerTouchArea(exitBtn);
		
		registerUpdateHandler(updateHandler);
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent)
	{
		// TODO Auto-generated method stub
		if(pSceneTouchEvent.isActionUp())
		{
			startBtn.setCurrentTileIndex(0);
			creditBtn.setCurrentTileIndex(0);
			exitBtn.setCurrentTileIndex(0);
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void onDisposed()
	{
		// TODO Auto-generated method stub
		unregisterUpdateHandler(updateHandler);
		updateHandler = null;
		
		bg.detachSelf();
		bg.dispose();

		logo.detachSelf();
		logo.dispose();

		this.detachSelf();
		this.dispose();
	}

	@Override
	public SceneType getSceneType()
	{
		// TODO Auto-generated method stub
		return SceneType.MAINMENU;
	}
}
