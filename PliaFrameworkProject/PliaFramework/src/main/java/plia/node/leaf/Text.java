package plia.node.leaf;

import android.util.Log;

import java.util.ArrayList;

import plia.graphics.Canvas;
import plia.graphics.Color3;
import plia.graphics.Rectangle;
import plia.graphics.SpriteFont;
import plia.graphics.Texture;
import plia.graphics.math.Vector2;
import plia.manager.SpriteFontManager;
import plia.node.base.Leaf;

import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDeleteFramebuffers;

public class Text extends Leaf
{
	private String text;
	private float fontSize;
	private SpriteFont spriteFont;
	private Color3 color;

    private boolean isChanged = false;
    private ArrayList<ArrayList<Word>> textLines;

    private boolean autoScrollDown;
    private int lineFocus;
    private int availableLinesInView;

//    private ArrayList<Integer[]> textures;

	public Text()
	{
		// TODO Auto-generated constructor stub
		spriteFont = SpriteFontManager.getInstance().getSpriteFont("default");
		fontSize = spriteFont.getDefaultFontSize();
		color = new Color3(1, 1, 1);

        textLines = new ArrayList<ArrayList<Word>>();
        lineFocus = 0;
        autoScrollDown = true;

//        textures = new ArrayList<Integer[]>();
	}

    @Override
    public void update(float elapsedTime)
    {

    }

    @Override
	public void draw(Canvas canvas)
	{
		// TODO Auto-generated method stub
//        if(isChanged)
//        {
//            if(textures.size() > 0)
//            {
//                int[] textureBuffers = new int[textures.size()];
//                int[] frameBuffers = new int[textures.size()];
//                for (int i = 0; i < textureBuffers.length; i++)
//                {
//                    textureBuffers[i] = textures.get(i)[0];
//                    frameBuffers[i] = textures.get(i)[1];
//                }
//
//                glDeleteBuffers(textureBuffers.length, textureBuffers, 0);
//                glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
//
//            }
//
//            textures = canvas.convertTextToTexture(text, spriteFont, fontSize, getSize());
//            setChanged(false);
//        }
//
////        canvas.drawTextTexture(textures.get(0), getPosition().x, getPosition().y, getSize().x, getSize().y, color);
//
//        for (int i = 0; i < textures.size(); i++)
//        {
//            int texID = textures.get(i)[0];
//            float w = getSize().x;
//            float h = (float)spriteFont.getFontHeight() / canvas.getWindowSize().y;
//            float x = getPosition().x;
//            float y = getPosition().y + (i * h);
//
//            canvas.drawTextTexture(texID, x, y, w, h, color);
////            if(y + h < getSize().y)
//
//        }

        if(isChanged)
        {
            textLines.clear();
            lineFocus = 0;
            availableLinesInView = 0;
            boolean endCount = false;
            ArrayList<Word> line = new ArrayList<Word>();

            float x = 0, y = 0;
            for (int i = 0; i < text.length(); i++)
            {
                Texture texture = spriteFont.get(text.charAt(i));
                float w = texture.getWidth() / canvas.getWindowSize().x;
                float h = texture.getHeight() / canvas.getWindowSize().y;

                if(x + w > getSize().x)
                {
                    y += h;
                    x = 0;

                    textLines.add(line);
                    line = new ArrayList<Word>();

                    if(!endCount)
                        availableLinesInView++;
                }

                if(y + h > getSize().y)
                {
                    endCount = true;
                }

//                canvas.drawSprite(texture, (x+getPosition().x), (y+getPosition().y), w, h, color);
                Word word = new Word(texture, x, y, w, h);
                line.add(word);

                x += w;
            }

            Log.println(Log.ASSERT, "State", "OnChanged");
            setChanged(false);
        }



        if(textLines.size() > 0)
        {
//            for(int i = lineFocus; i < lineFocus + availableLinesInView; i++)
//            {
//                float my = (i - lineFocus) * (spriteFont.getFontHeight() / canvas.getWindowSize().y);
//                for (Word word : textLines.get(i))
//                {
//                    canvas.drawSprite(word.texture, getPosition().x + word.x, getPosition().y + my, word.w, word.h, color);
//                }
//            }


            for(int i = lineFocus; i < lineFocus + availableLinesInView; i++)
            {
                ArrayList<Word> line = textLines.get(i);
                float my = (i - lineFocus) * (spriteFont.getFontHeight() / canvas.getWindowSize().y);
                for (Word word : line)
                {
                    canvas.drawSprite(word.texture, getPosition().x + word.x, getPosition().y + my, word.w, word.h, color);
                }
            }
//            int needLine = lineFocus + availableLinesInView;
//            int line = (needLine < textLines.size()) ? lineFocus :
        }

//        if(autoScrollDown)
//            scrollDown();
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(Object text)
	{
		this.text = text.toString();
        setChanged(true);
	}
	
	public void append(Object text)
	{
		this.text += text.toString();
	}
	
	public SpriteFont getSpriteFont()
	{
		return spriteFont;
	}
	
	public void setSpriteFont(SpriteFont spriteFont)
	{
		this.spriteFont = spriteFont;
        setChanged(true);
	}
	
	public void setFontSize(float fontSize)
	{
		this.fontSize = fontSize;
        setChanged(true);
	}
	
	public float getFontSize()
	{
		return fontSize;
	}
	
	public Color3 getColor()
	{
		return color;
	}
	
	public void setColor(Color3 color)
	{
		this.color = color;
	}
	
	public void setColor(float r, float g, float b)
	{
		this.color.set(r, g, b);
	}

    public void setAutoScrollDown(boolean autoScrollDown)
    {
        this.autoScrollDown = autoScrollDown;
    }

    public boolean isAutoScrollDown()
    {
        return autoScrollDown;
    }

    public void scrollUp()
    {
        if(lineFocus > 0)
        {
            lineFocus--;
        }
    }

    public void scrollDown()
    {
        if(lineFocus < textLines.size() - availableLinesInView)
        {
            lineFocus++;
        }
    }

    private void setChanged(boolean changed)
    {
        isChanged = changed;
    }

    class Word
    {
        Texture texture;
        float x, y, w, h;

        Word(Texture tex, float x, float y, float w, float h)
        {
            this.texture = tex;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}
