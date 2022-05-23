attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
uniform vec2 screensize;
uniform int yoffset;
varying vec4 v_color;
varying vec2 v_texCoords;
void main(void) {
	v_color = a_color;
	v_texCoords = a_texCoord0;
	float x = a_position.x + (screensize.x / 2);
	float y = a_position.y + (screensize.y / 2);
	gl_Position = u_projTrans * vec4(x, y, 0 , 1);
}