precision mediump float;
uniform sampler2D baseTexture;
varying vec2 uvCoord;

void main()
{
	gl_FragColor = texture2D(baseTexture, uvCoord);
}