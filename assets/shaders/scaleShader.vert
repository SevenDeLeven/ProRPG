#version 330 core
#extension GL_ARB_explicit_uniform_location : enable

layout(location=0)
uniform mat4 model;

layout(location=0);
in vec2 position;
layout(location=1);
in vec2 v_uv;

out vec2 f_uv;

void main() {
  f_uv = v_uv;
  
}
