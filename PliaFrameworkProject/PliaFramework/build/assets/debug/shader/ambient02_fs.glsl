precision mediump float;
uniform sampler2D baseTexture;
uniform vec4 color;
varying vec2 uvCoord;

void main()
{
	gl_FragColor = vec4(color.r, color.g, color.b, texture2D(baseTexture, uvCoord).w);
}