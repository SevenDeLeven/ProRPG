#version 330 core
#extension GL_ARB_explicit_uniform_location : enable

layout(location=0)
uniform vec4 xywh;
layout(location=1)
uniform mat4 model;
layout(location=4)
uniform mat4 projection;

layout(location=0)
in vec2 position;
layout(location=1)
in vec2 v_uv;

out vec2 f_uv;

void main() {
  f_uv = vec2(xywh.x+(v_uv.x*xywh.z),xywh.y+(v_uv.y*xywh.w));
  gl_Position = projection * model * vec4(position, 0.1, 1);
}
