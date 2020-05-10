package entity;

import shader.ShaderProgram;
import shader.UniformMatrix;
import shader.UniformSampler;
import shader.UniformVec3;
import shader.UniformVec4;

public class EntityShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "entity/entity.vert";
	private static final String FRAGMENT_SHADER = "entity/entity.frag";

	public UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	public UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformSampler diffuse = new UniformSampler("diffuse");
	public UniformVec3 lightDirection = new UniformVec3("lightDirection");
	public UniformVec3 color = new UniformVec3("color");
	public UniformVec4 clipSpace = new UniformVec4("clipSpace");

	public EntityShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_vertices", "in_uvs", "in_normals");
		super.storeAllUniformLocations(projectionViewMatrix, modelMatrix, diffuse, lightDirection, color, clipSpace);
	}
}
