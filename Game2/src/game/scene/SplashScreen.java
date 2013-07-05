package game.scene;

import game.core.Resources;
import game.manager.ResourcesManager;
import game.manager.SceneManager.SceneType;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SplashScreen extends BaseScene
{
	private Sprite bg, logo, nowLoading;
	private AnimatedSprite dot;
	
	private float sin;
	private IUpdateHandler updateHandler;

	@Override
	public void onCreated()
	{
		// TODO Auto-generated method stub
		ResourcesManager resourcesManager = ResourcesManager.getInstance();
		resourcesManager.loadSplashScreen();
		
		VertexBufferObjectManager vboManager = resourcesManager.getVertexBufferObjectManager();
		
		ITextureRegion bgTexture = resourcesManager.getTexture(Resources.SPLASHSCREEN_BACKGROUND);
		ITextureRegion logoTexture = resourcesManager.getTexture(Resources.SPLASHSCREEN_LOGO);
		ITextureRegion nowLoadingTexture = resourcesManager.getTexture(Resources.SPLASHSCREEN_NOWLOADING_TEXT);
		ITiledTextureRegion dotTexture = resourcesManager.getTileTexture(Resources.SPLASHSCREEN_LOADING_DOT);
		
		bg = new Sprite(0, 0, bgTexture, vboManager);
		
		logo = new Sprite(0, 0, logoTexture, vboManager);
		logo.setSkewCenterY(288);
		logo.setPosition(0, 150);
		
		nowLoading = new Sprite(0, 0, nowLoadingTexture, vboManager);
		nowLoading.setPosition(520, 560);
		
		dot = new AnimatedSprite(0, 0, dotTexture, vboManager);
		dot.setPosition(720, 580);
		dot.animate(500);
		
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
				sin+=0.05f;
				
			}
		};
		
		attachChild(bg);
		attachChild(logo);
		attachChild(nowLoading);
		attachChild(dot);
		
		registerUpdateHandler(updateHandler);
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
		
		nowLoading.detachSelf();
		nowLoading.dispose();
		
		dot.detachSelf();
		dot.dispose();
		
		this.detachSelf();
		this.dispose();
	}
	
	@Override
	public SceneType getSceneType()
	{
		// TODO Auto-generated method stub
		return SceneType.SPLASHSCREEN;
	}
}
