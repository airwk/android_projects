package plia.framework;

import android.opengl.GLSurfaceView;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import plia.graphics.Canvas;
import plia.interfaces.IState;
import plia.node.base.Leaf;
import plia.node.group.Layer;
import plia.node.group.Level;
import plia.node.group.Scene;

/**
 * Created by Wirune on 1/9/2556.
 */
public class FrameworkRenderer implements GLSurfaceView.Renderer, IState
{
    private Framework framework;
    private int state = 0;
    private int width, height;
    private Canvas canvas;
    private GameTime gameTime;

    private Level currentLevel;

    public FrameworkRenderer(Framework framework)
    {
        this.framework = framework;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)
    {
        state = 1;
        canvas = new Canvas();
        gameTime = GameTime.getInstance();
        gameTime.start();

        create();;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h)
    {
        this.width = w;
        this.height = h;
        this.canvas.setWindowSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10)
    {
        if(state == 3)
        {
            gameTime.update();
            glViewport(0, 0, width, height);
            glClearColor(1, 0.5f, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glDisable(GL_DEPTH_TEST);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            if(currentLevel != null)
            {
                currentLevel.update(GameTime.getElapsedGameTime().getMilliseconds());
                currentLevel.draw(canvas);
            }
        }
        else if(state == 2)
        {
            onResume();
        }
        else if(state == 4)
        {
            onPause();
        }
    }

    @Override
    public void create()
    {
        framework.create();
        resume();
    }

    @Override
    public void resume()
    {
        state = 2;
    }

    @Override
    public void pause()
    {
        state = 4;
    }

    @Override
    public void destroy()
    {
        state = 6;
    }

    //
    private void onResume()
    {
        framework.textureManager().resume();
        framework.shaderManager().resume();
        framework.spriteFontManager().resume();
        framework.onResume();
        state = 3;
    }

    private void onPause()
    {
        framework.textureManager().pause();
        framework.shaderManager().pause();
        framework.spriteFontManager().pause();
        framework.onPause();
        state = 5;
    }

    public Level getCurrentLevel()
    {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel)
    {
        this.currentLevel = currentLevel;
    }
}
