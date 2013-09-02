package plia.graphics;

import java.util.HashMap;

import plia.manager.TextureManager;
import plia.framework.BaseObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

/**
 * Created by Wirune on 1/9/2556.
 */
public class SpriteFont extends BaseObject
{
    private HashMap<Character, Texture> textures = new HashMap<Character, Texture>();

    private Paint paint;

    public SpriteFont(String id, Typeface typeface)
    {
        setID(id);
        paint = new Paint();
        paint.setTextSize(getDefaultFontSize());
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Align.LEFT);
        paint.setAntiAlias(true);

        if(typeface != null)
            paint.setTypeface(typeface);

        loop(32, 126);
    }

    private void loop(int start, int end)
    {
        for (int i = start; i <= end; i++)
        {
            char c = (char)(i);
            Bitmap bitmap = createBitmap(c);
            Texture texture = TextureManager.getInstance().createTexture(getID() + c, bitmap);
            textures.put(c, texture);
        }
    }

    private Bitmap createBitmap(char c)
    {
        int width = (int) (paint.measureText(c + "") + 0.5f); // round
        float baseline = (int) (-paint.ascent() + 1f); // ascent() is negative
        int height = (int) (baseline + paint.descent() + paint.descent() + 1f);

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawText(c + "", 0, baseline, paint);

        return bmp;
    }

    public Texture get(char c)
    {
        return textures.get(c);
    }

    public float getFontWidth(String text)
    {
        return paint.measureText(text);
    }

    public int getFontHeight()
    {
        float baseline = (int) (-paint.ascent() + 1f); // ascent() is negative
        int height = (int) (baseline + paint.descent() + paint.descent() + 1f);
        return height;
    }

    public int getDefaultFontSize()
    {
        return 32;
    }

    public Typeface getTypeface()
    {
        return paint.getTypeface();
    }
}