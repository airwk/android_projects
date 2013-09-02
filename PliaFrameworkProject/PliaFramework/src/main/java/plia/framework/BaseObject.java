package plia.framework;

/**
 * Created by Wirune on 1/9/2556.
 */
public class BaseObject
{
    private String id;

    protected BaseObject()
    {
        // TODO Auto-generated constructor stub
        id = hashCode() + "";
    }

    public String getID()
    {
        return id;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return "Object ID : " + id;
    }
}