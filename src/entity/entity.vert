#version 150

in vec3 in_vertices;
in vec2 in_uvs;
in vec3 in_normals;

out vec2 pass_uvs;
out vec3 pass_normals;

uniform vec4 clipSpace;
uniform mat4 projectionViewMatrix;
uniform mat4 modelMatrix;

void main(void) {

	vec4 worldPos = modelMatrix * vec4(in_vertices, 1.0);
	gl_Position = projectionViewMatrix * worldPos;
	
	gl_ClipDistance[0] = dot(worldPos, clipSpace);

	pass_uvs = in_uvs;
	pass_normals = normalize((vec4(in_normals, 1.0) * modelMatrix).xyz);
}
