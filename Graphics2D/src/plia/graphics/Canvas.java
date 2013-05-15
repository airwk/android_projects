package plia.graphics;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.Matrix;
import android.util.Log;

public final class Canvas
{
	private int shaderProgram;
	
	private float[] vertices;
	private float[] uv;
	private byte[] indices;
	
	private FloatBuffer vb;
	private FloatBuffer uvb;
	private ByteBuffer ib;
	
	private float[] modelViewProjectionMatrix;
	
	public Canvas()
	{
		// TODO Auto-generated constructor stub
		this.modelViewProjectionMatrix = new float[16];
		
		float[] modelView = new float[16];
		float[] projection = new float[16];
		
		Matrix.orthoM(projection, 0, 0, 1, 1, 0, 1, 10);
		Matrix.setLookAtM(modelView, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0);
		Matrix.multiplyMM(modelViewProjectionMatrix, 0, projection, 0, modelView, 0);
		
		this.vertices = new float[] { 0,0,  0,1,  1,1,  1,0 };
		this.uv = new float[] { 0,0,  0,1,  1,1,  1,0 };
		this.indices = new byte[] { 0,1,2,	0,2,3 };
		
		this.vb = ByteBuffer.allocateDirect( vertices.length * 4 ).order(ByteOrder.nativeOrder()).asFloatBuffer();
		this.vb.put(vertices).position(0);
		
		this.uvb = ByteBuffer.allocateDirect( uv.length * 4 ).order(ByteOrder.nativeOrder()).asFloatBuffer();
		this.uvb.put(uv).position(0);
		
		this.ib = ByteBuffer.allocateDirect( indices.length ).order(ByteOrder.nativeOrder());
		this.ib.put(indices).position(0);
		
		this.shaderProgram = createShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	public void drawSprite(Texture texture, PointF position, PointF size)
	{
		drawSprite(texture, position, size, null);
	}

	public void drawSprite(Texture texture, PointF position, PointF size, RectF source)
	{
		float[] transform = { size.x,0,0,0,  0,size.y,0,0,  0,0,1,0,  position.x,position.y,0,1 };
		drawSprite(texture, transform, source);
	}

	public void drawSprite(Texture texture, float[] transform, RectF source)
	{
		float[] mvpt = new float[16];
		
		Matrix.multiplyMM(mvpt, 0, modelViewProjectionMatrix, 0, transform, 0);

		if(source == null)
		{
			this.uvb.clear();
			this.uvb.put(uv).position(0);
		}
		else
		{
			float uvleft = source.left / texture.getWidth();
			float uvright = source.right / texture.getWidth();
			
			float uvtop = source.top / texture.getHeight();
			float uvbottom = source.bottom / texture.getHeight();
			
			float[] src = { uvleft,uvbottom, uvleft,uvtop, uvright,uvtop, uvright,uvbottom };
			
			this.uvb.clear();
			this.uvb.put(src).position(0);
		}
		
		glUseProgram(shaderProgram);

		int vh = glGetAttribLocation(shaderProgram, "vertex");
		int uvh = glGetAttribLocation(shaderProgram, "uv");
		
		glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "mvpMatrix"), 1, false, mvpt, 0);

		glEnableVertexAttribArray(vh);
		glVertexAttribPointer(vh, 2, GL_FLOAT, false, 0, vb);
		
		glEnableVertexAttribArray(uvh);
		glVertexAttribPointer(uvh, 2, GL_FLOAT, false, 0, uvb);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.getTextureBuffer());
		glUniform1i(glGetUniformLocation(shaderProgram, "baseTexture"), 0);
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, ib);

		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	
	
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
	
	private static final String VERTEX_SHADER = 
			"uniform mat4 mvpMatrix;" +
			"attribute vec4 vertex;" +
			"attribute vec2 uv;" +
			"varying vec2 uvCoord;" +
			"" +
			"void main()" +
			"{" +
			"	uvCoord = uv;" +
			"	gl_Position = mvpMatrix * vertex;" +
			"}";
	private static final String FRAGMENT_SHADER = 
			"precision mediump float;" +
			"uniform sampler2D baseTexture;" +
			"varying vec2 uvCoord;" +
			"void main()" +
			"{" +
			"	gl_FragColor = texture2D(baseTexture, uvCoord);" +
			"}";
}