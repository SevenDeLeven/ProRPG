#version 150 core

uniform sampler2D tex;

in vec2 f_uv;

out vec4 gl_FragColor;

void main() {
    gl_FragColor = texture(tex, f_uv);
}