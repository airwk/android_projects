package plia.manager;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_TRUE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import plia.graphics.Shader;

import android.util.Log;

/**
 * Created by Wirune on 1/9/2556.
 */
public class ShaderManager extends Manager
{
    private final HashMap<String, Shader> shaders = new HashMap<String, Shader>();

    private ShaderManager()
    {

    }

    @Override
    public void create()
    {

    }

    @Override
    public void resume()
    {
        for (Shader shader : shaders.values())
        {
            String vs = shader.getVertexShader();
            String fs = shader.getFragmentShader();

            shader.setProgram(createShaderProgram(vs, fs));
        }
    }

    @Override
    public void pause()
    {
        for (Shader shader : shaders.values())
        {
            glDeleteProgram(shader.getProgram());
        }
    }

    @Override
    public void destroy()
    {
        shaders.clear();
    }

    //
    public Shader getShader(String id)
    {
        if(shaders.containsKey(id))
            return shaders.get(id);

        return null;
    }

    public void createShader(String id, String[] shaderString)
    {
        createShader(id, shaderString[0], shaderString[1]);
    }

    public void createShader(String id, String vs, String fs)
    {
        Shader shader = new Shader(vs, fs);
        shaders.put(id, shader);
    }


    //

    private int createShaderProgram(String vs, String fs)
    {
        int vShader = loadShader(GL_VERTEX_SHADER, vs);
        int fShader = loadShader(GL_FRAGMENT_SHADER, fs);

        int program = glCreateProgram();

        glAttachShader(program, vShader);
        glAttachShader(program, fShader);
        glLinkProgram(program);

        int[] linkStatus = new int[1];

        glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GL_TRUE)
        {
            Log.e("Shader : "+program, "Could not link _program: ");
            Log.e("Shader : "+program, glGetProgramInfoLog(program));
            glDeleteProgram(program);
            program = 0;
            return 0;
        }

        return program;
    }

    private int loadShader(int shaderType, String src)
    {
        int shader = glCreateShader(shaderType);

        if(shader != 0)
        {
            glShaderSource(shader, src);
            glCompileShader(shader);
        }

        return shader;
    }

    private static ShaderManager instance = null;
    public static ShaderManager getInstance()
    {
        if(instance == null)
            instance = new ShaderManager();

        return instance;
    }
}
