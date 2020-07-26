#version 330 core

in vec2 position;
in vec2 v_uv;

out vec2 f_uv;

uniform mat4 model;
uniform mat4 projection;
//uniform mat4 view;

void main() {
	f_uv = v_uv;
	gl_Position = projection * model * vec4(position, 0.5, 1.0);
}
