package game.core;

import game.manager.ResourcesManager;
import game.manager.SceneManager;
import game.manager.SceneManager.SceneType;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;


public class GameActivity extends BaseGameActivity
{
	final int cameraWidth = 1280;
	final int cameraHeight = 720;

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions)
	{
		// TODO Auto-generated method stub
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public EngineOptions onCreateEngineOptions()
	{
		// TODO Auto-generated method stub
		Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);

		Boolean isFullScreen = true;
		ScreenOrientation screenOrientation = ScreenOrientation.LANDSCAPE_FIXED;
		RatioResolutionPolicy ratioResolutionPolicy = new RatioResolutionPolicy(cameraWidth, cameraHeight);

		EngineOptions engineOptions = new EngineOptions(isFullScreen, screenOrientation, ratioResolutionPolicy, camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception
	{
		// TODO Auto-generated method stub
		ResourcesManager.prepare(mEngine, this, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception
	{
		// TODO Auto-generated method stub
		SceneManager sceneManager = SceneManager.getInstance();
		sceneManager.setScene(SceneType.SPLASHSCREEN);
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.getCurrentScene());
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception
	{
		// TODO Auto-generated method stub
		mEngine.registerUpdateHandler(new TimerHandler(4f, new ITimerCallback()
		{
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler)
			{
				// TODO Auto-generated method stub
				mEngine.unregisterUpdateHandler(pTimerHandler);
				//
				ResourcesManager.getInstance().loadMainMenu();
				//
				SceneManager.getInstance().setScene(SceneType.MAINMENU);
				ResourcesManager.getInstance().unloadSplashScreen();
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		SceneManager.onDestroyed();
		ResourcesManager.onDestroyed();
	}
}
