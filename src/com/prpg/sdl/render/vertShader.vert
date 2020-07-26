#version 330 core

layout(location=0)
in vec2 position;

layout(location=1)
in vec2 v_uv;

out vec2 f_uv;

uniform mat4 model;
uniform mat4 projection;
//uniform mat4 view;
//uniform mat4 projection;

void main() {
//    mat4 mvp = projection * view * model;
	f_uv = v_uv;
    gl_Position = projection * model * vec4(position, 0.5, 1.0);
}
