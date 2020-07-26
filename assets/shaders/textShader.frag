#version 330 core

uniform sampler2D atlas;
uniform vec2 atlasSize;

in vec2 f_uv;

out vec4 gl_FragColor;

void main() {

  gl_FragColor = texture(atlas, vec2(f_uv.x/atlasSize.x, f_uv.y/atlasSize.y));
  // gl_FragColor = vec4(1,1,1,1);

}
