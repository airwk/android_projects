package game.manager;

import org.andengine.engine.Engine;

import game.scene.BaseScene;
import game.scene.MainMenu;
import game.scene.SplashScreen;

public class SceneManager
{
	private Engine engine;
	
	private BaseScene currentScene;
	private SceneType currentSceneType;

	private SceneManager()
	{
		currentSceneType = null;
	}
	
	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
	
	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}

	private void setScene(BaseScene scene, boolean isAnimate)
	{
		if(currentScene != scene)
		{
			if(currentScene != null)
				currentScene.onDisposed();

			if(engine == null)
				engine = ResourcesManager.getInstance().getEngine();
			
			engine.setScene(scene);
			currentScene = scene;
			
			if(isAnimate)
				currentScene.appearAnimation();
		}
	}
	
	public void setScene(SceneType sceneType)
	{
		if(sceneType == SceneType.SPLASHSCREEN)
		{
			setScene(new SplashScreen(), false);
			currentSceneType = sceneType;
		}
		else if(sceneType == SceneType.MAINMENU)
		{
			setScene(new MainMenu(), true);
			currentSceneType = sceneType;
		}
	}

	public static void onDestroyed()
	{
		INSTANCE = null;
	}
	
	private static SceneManager INSTANCE = null;
	public static SceneManager getInstance()
	{
		if(INSTANCE == null)
			INSTANCE = new SceneManager();
		
		return INSTANCE;
	}
	
	public enum SceneType
	{
		SPLASHSCREEN, MAINMENU
	}
}
