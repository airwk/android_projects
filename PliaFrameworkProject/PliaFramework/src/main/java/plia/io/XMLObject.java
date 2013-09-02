package plia.io;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Wirune on 1/9/2556.
 */
public class XMLObject
{
    private String name;
    private HashMap<String, Object> map;
    private HashMap<String, ArrayList<XMLObject>> children;

    public XMLObject(String name)
    {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.map = new HashMap<String, Object>();
        this.children = new HashMap<String, ArrayList<XMLObject>>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int size()
    {
        return map.size() + children.size();
    }

    public void add(String name, Object value)
    {
        map.put(name, value);
    }

    public boolean has(String name)
    {
        return map.containsKey(name) || children.containsKey(name);
    }

    public Object get(String name)
    {
        return map.get(name);
    }

    public String getString(String name)
    {
        return map.get(name).toString();
    }

    public int getInt(String name)
    {
        return Integer.parseInt(map.get(name).toString());
    }

    public float getFloat(String name)
    {
        return Float.parseFloat(map.get(name).toString());
    }

    public float[] getFloatArray(String name)
    {
        String[] split = map.get(name).toString().split(",");
        float[] arr = new float[split.length];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = Float.parseFloat(split[i].trim());
        }
        return arr;
    }

    public List<XMLObject> getXMLObjects(String name)
    {
        if (!children.containsKey(name))
        {
            children.put(name, new ArrayList<XMLObject>());
        }

        return children.get(name);
    }


    ///
    public static XMLObject parse(InputStream in)
    {
        try
        {
            XmlPullParser parser = createParser(in);
            return read(parser, null);
        }
        catch (XmlPullParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new XMLObject("");
    }

    private static XmlPullParser createParser(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            return parser;
        }
        finally
        {
            in.close();
        }
    }

    private static XMLObject read(XmlPullParser parser, XMLObject parent) throws XmlPullParserException, IOException
    {
        XMLObject element = readElement(parser);

        while (parser.getEventType() != XmlPullParser.END_TAG ||
                !(parser.getName().equals(element.getName())))
        {
            if (parser.nextTag() == XmlPullParser.START_TAG)
            {
                read(parser, element);
            }
        }

        if (parent != null)
        {
            parent.getXMLObjects(element.getName()).add(element);
            return parent;
        }

        return element;
    }

    private static XMLObject readElement(XmlPullParser parser)
    {
        String elementName = parser.getName();
        int attribCount = parser.getAttributeCount();

        XMLObject element = new XMLObject(elementName);

        for (int i = 0; i < attribCount; i++)
        {
            String attributeName = parser.getAttributeName(i);
            String attributeValue = parser.getAttributeValue(i);

            element.add(attributeName, attributeValue);
        }

        return element;
    }
    //

}
