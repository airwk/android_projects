package plia.graphics;

/**
 * Created by Wirune on 1/9/2556.
 */
public class Rectangle
{
    public float x, y, width, height;

    public Rectangle()
    {
        // TODO Auto-generated constructor stub
    }

    public Rectangle(float x, float y, float w, float h)
    {
        // TODO Auto-generated constructor stub
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public float getLeft()
    {
        return x;
    }
    public float getRight()
    {
        return x + width;
    }
    public float getTop()
    {
        return y;
    }
    public float getBottom()
    {
        return y + height;
    }

}