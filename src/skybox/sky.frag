#version 150

in vec3 pass_uvs;
uniform samplerCube sampler;
out vec4 out_colour;
in float pass_bgAlpha;

uniform vec3 topColor;
uniform vec3 bottomColor;

void main(void){
	vec4 finalColor = texture(sampler, pass_uvs);
	vec4 bgColor = vec4(mix(topColor, bottomColor, -pass_uvs.y/100.0),1.0);
	if (finalColor.a == 1.0) {
		out_colour = mix(bgColor, finalColor, pass_bgAlpha);
	} else {
		out_colour = bgColor;
	}
	
}
