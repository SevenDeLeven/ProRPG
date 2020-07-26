#version 330 core
#extension GL_ARB_explicit_uniform_location : enable

layout(location=0)
in vec2 position;

layout(location=1)
in vec2 v_uv;

out vec2 f_uv;

layout(location=0)
uniform mat4 model;
layout(location=1)
uniform mat4 projection;
//uniform mat4 view;

void main() {
	f_uv = v_uv;
	gl_Position = projection * model * vec4(position, 0.5, 1.0);
}
