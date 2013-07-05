package game.manager;

import java.util.HashMap;

import game.core.GameActivity;
import game.core.Resources;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public final class ResourcesManager
{
	private Engine engine;
	private GameActivity activity;
	private VertexBufferObjectManager vboManager;
	
	//
	private HashMap<String, ITextureRegion> textures;
	private HashMap<String, ITiledTextureRegion> tileTextures;
	private HashMap<String, BitmapTextureAtlas> atlases;
	
	private ResourcesManager()
	{
		textures = new HashMap<String, ITextureRegion>();
		tileTextures = new HashMap<String, ITiledTextureRegion>();
		atlases = new HashMap<String, BitmapTextureAtlas>();
	}
	
	public void loadGraphics()
	{
		
	}
	
	public void loadMainMenu()
	{
		createTexture(Resources.MAINMENU_BACKGROUND, TextureOptions.DEFAULT, 0, 0, 1280, 720);
		createTexture(Resources.MAINMENU_LOGO, TextureOptions.BILINEAR_PREMULTIPLYALPHA, 0, 0, 1000, 262);
		createTileTexture(Resources.MAINMENU_START_BUTTON, TextureOptions.BILINEAR_PREMULTIPLYALPHA, 0, 0, 800, 159, 2, 1);
		createTileTexture(Resources.MAINMENU_CREDIT_BUTTON, TextureOptions.BILINEAR_PREMULTIPLYALPHA, 0, 0, 800, 159, 2, 1);
		createTileTexture(Resources.MAINMENU_EXIT_BUTTON, TextureOptions.BILINEAR_PREMULTIPLYALPHA, 0, 0, 210, 159, 2, 1);
		
	}

	public void loadSplashScreen()
	{
		//BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(Resources.SPLASHSCREEN_ROOTPATH);
		createTexture(Resources.SPLASHSCREEN_BACKGROUND, TextureOptions.DEFAULT, 0, 0, 1280, 720);
		createTexture(Resources.SPLASHSCREEN_LOGO, TextureOptions.BILINEAR_PREMULTIPLYALPHA, 0, 0, 1280, 288);
		createTexture(Resources.SPLASHSCREEN_NOWLOADING_TEXT, TextureOptions.DEFAULT, 0, 0, 202, 43);
		createTileTexture(Resources.SPLASHSCREEN_LOADING_DOT, TextureOptions.DEFAULT, 0, 0, 84, 12, 3, 1);
	}
	
	public void unloadSplashScreen()
	{
		atlases.remove(Resources.SPLASHSCREEN_BACKGROUND).unload();
		atlases.remove(Resources.SPLASHSCREEN_LOGO).unload();
		atlases.remove(Resources.SPLASHSCREEN_NOWLOADING_TEXT).unload();
		atlases.remove(Resources.SPLASHSCREEN_LOADING_DOT).unload();

		textures.remove(Resources.SPLASHSCREEN_BACKGROUND);
		textures.remove(Resources.SPLASHSCREEN_LOGO);
		textures.remove(Resources.SPLASHSCREEN_NOWLOADING_TEXT);
		tileTextures.remove(Resources.SPLASHSCREEN_LOADING_DOT);
	}
	
	//
	private void createTexture(String fileName, TextureOptions option, int tx, int ty, int w, int h)
	{
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(activity.getTextureManager(), w, h, option);
		ITextureRegion texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, fileName, tx, ty);
		
		atlas.load();
		
		textures.put(fileName, texture);
		atlases.put(fileName, atlas);
	}
	
	private void createTileTexture(String fileName, TextureOptions option, int tx, int ty, int w, int h, int column, int row)
	{
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(activity.getTextureManager(), w, h, option);
		ITiledTextureRegion texture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, activity, fileName, tx, ty, column, row);
		
		atlas.load();
		
		tileTextures.put(fileName, texture);
		atlases.put(fileName, atlas);
	}
	
	public ITextureRegion getTexture(String fileName)
	{
		return textures.get(fileName);
	}
	
	public ITiledTextureRegion getTileTexture(String fileName)
	{
		return tileTextures.get(fileName);
	}
	
	//
	public Engine getEngine()
	{
		return engine;
	}
	
	public GameActivity getActivity()
	{
		return activity;
	}
	
	public VertexBufferObjectManager getVertexBufferObjectManager()
	{
		return vboManager;
	}
	
	public static void prepare(Engine engine, GameActivity activity, VertexBufferObjectManager vboManager)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().vboManager = vboManager;
	}
	
	public static void onDestroyed()
	{
		INSTANCE = null;
	}

	private static ResourcesManager INSTANCE = null;
	public static ResourcesManager getInstance()
	{
		if(INSTANCE == null)
			INSTANCE = new ResourcesManager();
		
		return INSTANCE;
	}
}
