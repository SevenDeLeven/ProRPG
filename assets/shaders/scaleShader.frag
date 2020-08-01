#version 330 core

uniform vec3 color;
uniform vec3 outline;
uniform vec2 size;
uniform sampler2D tex;
uniform vec2 atlasSize;

in vec2 f_uv;

out vec4 gl_FragColor;

void main() {
	vec4 texColor = texture(tex, f_uv);
	gl_FragColor = vec4((color*texColor.g)+(outline*texColor.r), texColor.a);
	if (gl_FragColor.a <= 0.01)
		discard;
}

