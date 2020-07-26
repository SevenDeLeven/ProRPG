#version 330 core

uniform mat4 model;

in vec2 position;
in vec2 v_uv;

out vec2 f_uv;

void main() {
  f_uv = v_uv;
  
}
