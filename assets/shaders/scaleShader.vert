#version 330 core

uniform mat4 model;
uniform mat4 projection;
uniform int scale;
uniform vec2 atlasSize;
uniform vec2 scaleSize;

in vec2 position;
in vec2 v_uv;

out vec2 f_uv;

void main() {
  f_uv = vec2((scaleSize.x/atlasSize.x)*v_uv.x, (scaleSize.y/atlasSize.y)*v_uv.y);
  //f_uv = vev_uv;
  gl_Position = projection * model * vec4(position, 0.3, 1);
}
