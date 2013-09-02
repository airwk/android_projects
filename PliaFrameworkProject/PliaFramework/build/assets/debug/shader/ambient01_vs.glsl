uniform mat4 mvpMatrix;
attribute vec4 vertex;
attribute vec2 uv;
varying vec2 uvCoord;

void main()
{
	uvCoord = uv;
	gl_Position = mvpMatrix * vertex;
}