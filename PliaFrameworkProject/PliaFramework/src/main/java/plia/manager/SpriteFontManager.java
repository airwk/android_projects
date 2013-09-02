package plia.manager;

import java.util.HashMap;

import plia.graphics.SpriteFont;
import android.graphics.Typeface;

/**
 * Created by Wirune on 1/9/2556.
 */
public class SpriteFontManager extends Manager
{
    private final HashMap<String, SpriteFont> spriteFonts = new HashMap<String, SpriteFont>();

    private SpriteFontManager()
    {

    }

    @Override
    public void create()
    {
        String id = "default";
        SpriteFont spriteFont = new SpriteFont(id, null);
        spriteFonts.put(id, spriteFont);
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void destroy()
    {
        spriteFonts.clear();
    }

    public SpriteFont createSpriteFont(String id, String assetPath)
    {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), assetPath);
        SpriteFont spriteFont = new SpriteFont(id, typeface);
        spriteFonts.put(id, spriteFont);

        return spriteFonts.get(id);
    }

    public SpriteFont getSpriteFont(String id)
    {
        if(spriteFonts.containsKey(id))
        {
            return spriteFonts.get(id);
        }
        return spriteFonts.get("default");
    }

    private static SpriteFontManager instance = null;
    public static SpriteFontManager getInstance()
    {
        if(instance == null)
            instance = new SpriteFontManager();

        return instance;
    }
}
