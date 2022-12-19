attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
varying vec4 v_color;
varying vec2 v_texCoords;
void main(void) {
	v_color = a_color;
	v_color.a = v_color.a * (255.0/254.0);
	v_texCoords = a_texCoord0;
	float x = -1.0 + a_position.x / 400.0;
    float y = 1.0 - a_position.y / 300.0;
    gl_Position = vec4(x, y, 0.0, 1.0);
}