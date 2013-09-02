package plia.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import plia.graphics.Texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Wirune on 1/9/2556.
 */
public class TextureManager extends Manager
{
    private final HashMap<String, Texture> textures = new HashMap<String, Texture>();

    private TextureManager()
    {
        // TODO Auto-generated constructor stub

    }

    @Override
    public void create()
    {

    }

    @Override
    public void resume()
    {
        for (Texture texture : textures.values())
        {
            texture.createTextureBuffer();
        }
    }

    @Override
    public void pause()
    {
        for (Texture texture : textures.values())
        {
            texture.deleteTextureBuffer();
        }
    }

    @Override
    public void destroy()
    {
        textures.clear();
    }

    public Texture createTexture(String id, String texturePath)
    {
        if(!textures.containsKey(id))
        {
            try
            {
                InputStream in = getContext().getAssets().open(texturePath);
                Bitmap bitmap = BitmapFactory.decodeStream(in);

                Texture texture = new Texture(id, bitmap);
                textures.put(id, texture);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                Log.e("Error", "IOException");
            }
        }

        return textures.get(id);
    }

    public Texture createTexture(String id, Bitmap bitmap)
    {
        if(!textures.containsKey(id))
        {
            Texture texture = new Texture(id, bitmap);
            textures.put(id, texture);
        }

        return textures.get(id);
    }

    private static TextureManager instance = null;
    public static TextureManager getInstance()
    {
        if(instance == null)
            instance = new TextureManager();

        return instance;
    }
}
