package plia.framework;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import plia.io.XMLObject;
import plia.manager.ShaderManager;
import plia.manager.SpriteFontManager;

/**
 * Created by Wirune on 1/9/2556.
 */
public class FrameworkSettings
{
    public static final String SETTING_ROOTPATH = "settings";
    public static final String SHADER_ROOTPATH = "shader";
    public static final String SPRITEFONT_ROOTPATH = "spritefont";

    public static final String SHADER_FILE = "shader.xml";
    public static final String SPRITEFONT_FILE = "spritefont.xml";

    public static void loadSettings(AssetManager assetManager)
    {
        try
        {
            XMLObject shaderXML = loadSettingXML(assetManager, SHADER_FILE);
            XMLObject spriteFontXML = loadSettingXML(assetManager, SPRITEFONT_FILE);

            loadShader(assetManager, shaderXML);
            loadSpriteFont(spriteFontXML);
        }
        catch (IOException ex)
        {
            Log.println(Log.ASSERT, "Framework Settings", "Error IOException");
        }
    }

    private static XMLObject loadSettingXML(AssetManager assetManager, String filename) throws IOException
    {
        XMLObject xmlObject = XMLObject.parse(assetManager.open(SETTING_ROOTPATH+"/"+filename));
        return xmlObject;
    }

    private static void loadShader(AssetManager assetManager, XMLObject xmlObject)
    {
        List<XMLObject> programs = xmlObject.getXMLObjects("program");
        for (XMLObject program : programs)
        {
            String id = program.getString("id");
            String vsPath = SHADER_ROOTPATH + "/" + id + "_vs.glsl";
            String fsPath = SHADER_ROOTPATH + "/" + id + "_fs.glsl";
            String vs = loadString(assetManager, vsPath);
            String fs = loadString(assetManager, fsPath);

            ShaderManager.getInstance().createShader(id, vs, fs);
        }
    }

    private static void loadSpriteFont(XMLObject xmlObject)
    {
        List<XMLObject> fonts = xmlObject.getXMLObjects("font");
        for (XMLObject font : fonts)
        {
            String id = font.getString("id");
            String filename = font.getString("filename");
            String path = SPRITEFONT_ROOTPATH + "/" + filename;
            SpriteFontManager.getInstance().createSpriteFont(id, path);
        }
    }

    private static String loadString(AssetManager assetManager, String assetPath)
    {
        try
        {
            InputStream ins = assetManager.open(assetPath);
            InputStreamReader isr = new InputStreamReader(ins);
            char[] buffer = new char[ins.available()];

            isr.read(buffer, 0, buffer.length);
            return new String(buffer);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
