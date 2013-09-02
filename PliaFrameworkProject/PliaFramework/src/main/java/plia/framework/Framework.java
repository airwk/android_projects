package plia.framework;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;

import android.annotation.SuppressLint;
import android.util.Log;

import plia.interfaces.*;
import plia.manager.ShaderManager;
import plia.manager.SpriteFontManager;
import plia.manager.TextureManager;
import plia.node.group.Level;

/**
 * Created by Wirune on 1/9/2556.
 */
public abstract class Framework implements IState, IOnState
{
    private Activity activity;
    private GLSurfaceView glSurfaceView;
    private FrameworkRenderer renderer;

    @SuppressLint("NewApi")
    public Framework(Activity activity)
    {
        this.activity = activity;
        this.textureManager().setContext(activity);
        this.shaderManager().setContext(activity);
        this.spriteFontManager().setContext(activity);

        FrameworkSettings.loadSettings(activity.getAssets());

        renderer = new FrameworkRenderer(this);
        glSurfaceView = new GLSurfaceView(activity);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(renderer);

        activity.setContentView(glSurfaceView);
    }

    @Override
    public final void create()
    {
        textureManager().create();
        shaderManager().create();
        spriteFontManager().create();
        this.onCreate();
    }

    @Override
    public final void resume()
    {
        renderer.resume();
    }

    @Override
    public final void pause()
    {
        renderer.pause();
    }

    @Override
    public final void destroy()
    {
        textureManager().destroy();
        shaderManager().destroy();
        spriteFontManager().destroy();
        this.onDestroy();
    }

    public void setLevel(Level level)
    {
        renderer.setCurrentLevel(level);
    }

    public Activity getActivity()
    {
        return activity;
    }

    public TextureManager textureManager()
    {
        return TextureManager.getInstance();
    }

    public ShaderManager shaderManager()
    {
        return ShaderManager.getInstance();
    }

    public SpriteFontManager spriteFontManager()
    {
        return SpriteFontManager.getInstance();
    }

    public void trace(Object msg)
    {
        Log.println(Log.ASSERT, "Trace", msg + "");
    }
}

