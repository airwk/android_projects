package plia.graphics;

/**
 * Created by Wirune on 1/9/2556.
 */
public class Shader
{
    private String vertexShader;
    private String fragmentShader;
    private int program;

    public Shader(String vs, String fs)
    {
        // TODO Auto-generated constructor stub
        this.vertexShader = vs;
        this.fragmentShader = fs;
    }

    public int getProgram()
    {
        return program;
    }

    public void setProgram(int program)
    {
        this.program = program;
    }

    public String getVertexShader()
    {
        return vertexShader;
    }

    public String getFragmentShader()
    {
        return fragmentShader;
    }
}