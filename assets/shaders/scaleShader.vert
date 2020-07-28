#version 330 core

uniform vec4 sector;
uniform mat4 model;
uniform mat4 projection;
uniform int scale;

in vec2 position;
in vec2 v_uv;

out vec2 f_uv;

void main() {
  float width = projection*model*vec4(0,);
  f_uv = vec2(sector.x+(v_uv.x*(sector.z*scale)));
  gl_Position = projection * model * vec4(position, 0.2, 1);
}
