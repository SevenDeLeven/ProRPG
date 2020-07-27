#version 330 core

uniform ivec4 xywh;
uniform mat4 model;
uniform mat4 projection;

in vec2 position;
in vec2 v_uv;

out vec2 f_uv;

void main() {
  f_uv = vec2(xywh.x+(v_uv.x*xywh.z),xywh.y+(v_uv.y*xywh.w));
  gl_Position = projection * model * vec4(position, 0.1, 1);
}
