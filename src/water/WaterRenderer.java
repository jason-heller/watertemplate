package water;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import core.Camera;
import opengl.fbo.FrameBuffer;
import utils.Model;
import utils.ObjLoader;
import utils.TextureLoader;

public class WaterRenderer {

	private WaterShader shader;
	private Model model;
	private int dudv;
	
	public WaterRenderer() {
		shader = new WaterShader();		// Init shader
		dudv = TextureLoader.createTexture("res/dudv.png");
		model = ObjLoader.loadObj("res/water.obj", true);
	}

	public void render(Camera camera, FrameBuffer reflectionFbo, FrameBuffer refractionFbo, float timer) {
		shader.start();

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		shader.reflection.loadTexUnit(0);
		shader.refraction.loadTexUnit(1);
		shader.dudv.loadTexUnit(2);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, reflectionFbo.getTextureBuffer());

		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, refractionFbo.getTextureBuffer());

		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudv);

		model.bind(0, 1);
		shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
		shader.timer.loadFloat(timer);

		for (int i = -2; i < 2; i++) {
			for (int j = -2; j < 2; j++) {
				shader.offset.loadVec4(i, j + 1, 512, 0);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
		GL11.glDeleteTextures(dudv);
		// TODO: delete model
	}
}
