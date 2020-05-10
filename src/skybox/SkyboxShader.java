package skybox;

import shader.ShaderProgram;
import shader.UniformFloat;
import shader.UniformMatrix;
import shader.UniformSampler;
import shader.UniformVec3;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "skybox/sky.vert";
	private static final String FRAGMENT_SHADER = "skybox/sky.frag";

	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformVec3 topColor = new UniformVec3("topColor");
	protected UniformVec3 bottomColor = new UniformVec3("bottomColor");
	protected UniformSampler sampler = new UniformSampler("sampler");
	protected UniformFloat bgAlpha = new UniformFloat("bgAlpha");

	public SkyboxShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, topColor, bottomColor, sampler, bgAlpha);
	}
}
