package plia.graphics;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import plia.manager.ShaderManager;
import plia.graphics.math.Vector2;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by Wirune on 1/9/2556.
 */
public class Canvas
{
    private Shader spriteShader;
    private Shader textShader;

    private float[] vertices;
    private float[] uv;
    private byte[] indices;

    private FloatBuffer vb;
    private FloatBuffer uvb;
    private ByteBuffer ib;

    private float[] modelViewProjectionMatrix;

    private Vector2 windowSize;

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

        this.spriteShader = ShaderManager.getInstance().getShader("ambient01");
        this.textShader = ShaderManager.getInstance().getShader("ambient02");
        this.windowSize = new Vector2(1, 1);
    }

    public Vector2 getWindowSize()
    {
        return windowSize;
    }

    public void setWindowSize(Vector2 windowSize)
    {
        this.windowSize = windowSize;
    }

    public void setWindowSize(float w, float h)
    {
        this.windowSize.set(w, h);
    }

    public void drawSprite(Texture texture, Vector2 position, Vector2 size)
    {
        drawSprite(texture, position, size, null);
    }

    public void drawSprite(Texture texture, Vector2 position, Vector2 size, Rectangle source)
    {
        float[] transform = { size.x,0,0,0,  0,size.y,0,0,  0,0,1,0,  position.x,position.y,0,1 };
        drawSprite(texture, transform, source);
    }

    public void drawSprite(Texture texture, float[] transform, Rectangle source)
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
            float uvleft = source.getLeft() / texture.getWidth();
            float uvright = source.getRight() / texture.getWidth();

            float uvtop = source.getTop() / texture.getHeight();
            float uvbottom = source.getBottom() / texture.getHeight();

            float[] src = { uvleft,uvbottom, uvleft,uvtop, uvright,uvtop, uvright,uvbottom };

            this.uvb.clear();
            this.uvb.put(src).position(0);
        }

        int shaderProgram = spriteShader.getProgram();
        glUseProgram(shaderProgram);

        int vh = glGetAttribLocation(shaderProgram, "vertex");
        int uvh = glGetAttribLocation(shaderProgram, "uv");

        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "mvpMatrix"), 1, false, mvpt, 0);

        glEnableVertexAttribArray(vh);
        glVertexAttribPointer(vh, 2, GL_FLOAT, false, 0, vb);

        glEnableVertexAttribArray(uvh);
        glVertexAttribPointer(uvh, 2, GL_FLOAT, false, 0, uvb);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        glUniform1i(glGetUniformLocation(shaderProgram, "baseTexture"), 0);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, ib);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void drawSprite(Texture texture, float x, float y, float w, float h, Color3 color)
    {
        float[] transform = { w,0,0,0,  0,h,0,0,  0,0,1,0,  x,y,0,1 };

        float[] mvpt = new float[16];

        Matrix.multiplyMM(mvpt, 0, modelViewProjectionMatrix, 0, transform, 0);

        this.uvb.clear();
        this.uvb.put(uv).position(0);

        int shaderProgram = spriteShader.getProgram();
        glUseProgram(shaderProgram);

        int vh = glGetAttribLocation(shaderProgram, "vertex");
        int uvh = glGetAttribLocation(shaderProgram, "uv");

        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "mvpMatrix"), 1, false, mvpt, 0);

        glEnableVertexAttribArray(vh);
        glVertexAttribPointer(vh, 2, GL_FLOAT, false, 0, vb);

        glEnableVertexAttribArray(uvh);
        glVertexAttribPointer(uvh, 2, GL_FLOAT, false, 0, uvb);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        glUniform1i(glGetUniformLocation(shaderProgram, "baseTexture"), 0);

        glUniform4f(glGetUniformLocation(shaderProgram, "color"), color.r, color.g, color.b, 1);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, ib);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

//    public void drawTexture(int texID, float x, float y, float w, float h, Color3 color, boolean isFlip)
//    {
//        float[] transform = { w,0,0,0,  0,h,0,0,  0,0,1,0,  x,y,0,1 };
//        float[] mvpt = new float[16];
//
//        Matrix.multiplyMM(mvpt, 0, modelViewProjectionMatrix, 0, transform, 0);
//
//        float uvleft = 0;
//        float uvright = 1;
//
//        float uvtop = (isFlip) ? 0 : 1;
//        float uvbottom = (isFlip) ? 1 : 0;
//
//        float[] src = { uvleft,uvbottom, uvleft,uvtop, uvright,uvtop, uvright,uvbottom };
//
//        this.uvb.clear();
//        this.uvb.put(src).position(0);
//
//        int shaderProgram = textShader.getProgram();
//        glUseProgram(shaderProgram);
//
//        int vh = glGetAttribLocation(shaderProgram, "vertex");
//        int uvh = glGetAttribLocation(shaderProgram, "uv");
//
//        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "mvpMatrix"), 1, false, mvpt, 0);
//
//        glEnableVertexAttribArray(vh);
//        glVertexAttribPointer(vh, 2, GL_FLOAT, false, 0, vb);
//
//        glEnableVertexAttribArray(uvh);
//        glVertexAttribPointer(uvh, 2, GL_FLOAT, false, 0, uvb);
//
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texID);
//        glUniform1i(glGetUniformLocation(shaderProgram, "baseTexture"), 0);
//
//        glUniform4f(glGetUniformLocation(shaderProgram, "color"), color.r, color.g, color.b, 1);
//
//        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, ib);
//
//        glBindTexture(GL_TEXTURE_2D, 0);
//    }

    public void drawTextTexture(int texID, float x, float y, float w, float h, Color3 color)
    {
        float[] transform = { w,0,0,0,  0,h,0,0,  0,0,1,0,  x,y,0,1 };
        float[] mvpt = new float[16];

        Matrix.multiplyMM(mvpt, 0, modelViewProjectionMatrix, 0, transform, 0);

        float uvleft = 0;
        float uvright = 1;

        float v0 = 1-h;

        float uvtop = 0;
        float uvbottom = 1;

        float[] src = { uvleft,uvbottom, uvleft,uvtop, uvright,uvtop, uvright,uvbottom };

        this.uvb.clear();
        this.uvb.put(src).position(0);

        int shaderProgram = textShader.getProgram();
        glUseProgram(shaderProgram);

        int vh = glGetAttribLocation(shaderProgram, "vertex");
        int uvh = glGetAttribLocation(shaderProgram, "uv");

        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "mvpMatrix"), 1, false, mvpt, 0);

        glEnableVertexAttribArray(vh);
        glVertexAttribPointer(vh, 2, GL_FLOAT, false, 0, vb);

        glEnableVertexAttribArray(uvh);
        glVertexAttribPointer(uvh, 2, GL_FLOAT, false, 0, uvb);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
        glUniform1i(glGetUniformLocation(shaderProgram, "baseTexture"), 0);

        glUniform4f(glGetUniformLocation(shaderProgram, "color"), color.r, color.g, color.b, 1);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, ib);

        glBindTexture(GL_TEXTURE_2D, 0);
    }


    //

    public ArrayList<Integer[]> convertTextToTexture(String text, SpriteFont spriteFont, float fontSize, Vector2 size)
    {
        int segmentX = (int)(size.x * windowSize.x);
        int segmentY = spriteFont.getFontHeight();//(int)windowSize.y;//(int)(getWindowSize().y + 0.5f);
        Color3 color = new Color3(0, 0, 0);

        ArrayList<Integer[]> textures = new ArrayList<Integer[]>();
        Integer[] buffer = bindFrameBuffer(segmentX, segmentY);
        textures.add(buffer);

        float fSize = fontSize / (float)spriteFont.getDefaultFontSize();
        float tx = 0, ty = 0;

        for (int i = 0; i < text.length(); i++)
        {
            Texture texture = spriteFont.get(text.charAt(i));
            float tw = (texture.getWidth() * fSize);
//            float th = (texture.getHeight() * fSize);
            float tsx = (tx / windowSize.x);
            float tsy = (ty / windowSize.y);
            float tsw = (tw / windowSize.x);
            float tsh = 1;//(th / windowSize.y);

            if(tsx + tsw > size.x)
            {
//                ty += th;
                tsy = (ty / windowSize.y);

                tx = 0;
                tsx = 0;

                buffer = bindFrameBuffer(segmentX, segmentY);
                textures.add(buffer);
            }

//            drawTexture(texture.getTextureID(), tsx, tsy, tsw, tsh, color, false);
            float[] transform = { tsw,0,0,0,  0,tsh,0,0,  0,0,1,0,  tsx,tsy,0,1 };
            float[] mvpt = new float[16];

            Matrix.multiplyMM(mvpt, 0, modelViewProjectionMatrix, 0, transform, 0);

            float uvleft = 0;
            float uvright = 1;

            float uvtop = 1;
            float uvbottom = 0;

            float[] src = { uvleft,uvbottom, uvleft,uvtop, uvright,uvtop, uvright,uvbottom };

            this.uvb.clear();
            this.uvb.put(src).position(0);

            int shaderProgram = textShader.getProgram();
            glUseProgram(shaderProgram);

            int vh = glGetAttribLocation(shaderProgram, "vertex");
            int uvh = glGetAttribLocation(shaderProgram, "uv");

            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "mvpMatrix"), 1, false, mvpt, 0);

            glEnableVertexAttribArray(vh);
            glVertexAttribPointer(vh, 2, GL_FLOAT, false, 0, vb);

            glEnableVertexAttribArray(uvh);
            glVertexAttribPointer(uvh, 2, GL_FLOAT, false, 0, uvb);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
            glUniform1i(glGetUniformLocation(shaderProgram, "baseTexture"), 0);

            glUniform4f(glGetUniformLocation(shaderProgram, "color"), color.r, color.g, color.b, 1);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, ib);

            glBindTexture(GL_TEXTURE_2D, 0);


            tx += tw;
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

//        int height = (int)((segmentY * textures.size()) + 0.5f);
//        int textureID = bindFrameBuffer(segmentX, height);
//        for (int i = 0; i < textures.size(); i++)
//        {
//            float sy = (segmentY / windowSize.y);
//            float posy = sy * i;
//            drawTexture(textures.get(i), 0, posy, 1, sy, color, true);
//        }
//
//        glBindFramebuffer(GL_FRAMEBUFFER, 0);
//        glBindTexture(GL_TEXTURE_2D, 0);
//
//        return textureID;
        return textures;
    }

    private Integer[] bindFrameBuffer(int w, int h)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        int[] frameBuffer = new int[1];
//        int[] depthRenderBuffer = new int[1];
        int[] renderTextureBuffer = new int[1];

        // generate
        glGenFramebuffers(1, frameBuffer, 0);
//        glGenRenderbuffers(1, depthRenderBuffer, 0);
        glGenTextures(1, renderTextureBuffer, 0);

        // generate color texture
        glBindTexture(GL_TEXTURE_2D, renderTextureBuffer[0]);

        // parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        // create an empty intbuffer first?
        int[] buf = new int[w * h];
        IntBuffer textureBuffer = ByteBuffer.allocateDirect(buf.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer);

        glGenerateMipmap(GL_TEXTURE_2D);

//        glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBuffer[0]);
//        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, w, h);

        // Bind Frame Buffer
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTextureBuffer[0], 0);

        // attach render buffer as depth buffer
//        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBuffer[0]);

//        float sx = windowSize.x / w;
//        int width = (int)(w * sx);
        glViewport(0, 0, (int)windowSize.x, h);
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//        glDeleteFramebuffers(frameBuffer.length, frameBuffer, 0);

        return new Integer[] { renderTextureBuffer[0], frameBuffer[0] };
    }
}