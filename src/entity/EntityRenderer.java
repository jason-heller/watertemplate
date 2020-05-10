package entity;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import core.Camera;

public class EntityRenderer {

	private static EntityShader shader;

	public static void cleanUp() {
		shader.cleanUp();
	}


	public static void init() {
		shader = new EntityShader();
	}


	public static void render(Camera camera, Vector3f lightDir, Entity entity, Vector4f clipPlane) {
		shader.start();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
		shader.lightDirection.loadVec3(lightDir);
		shader.color.loadVec3(1, 1, 1);
		shader.clipSpace.loadVec4(clipPlane);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.texture);
		entity.model.bind(0, 1, 2);
		shader.modelMatrix.loadMatrix(entity.getMatrix());

		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.model.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		entity.model.unbind(0, 1, 2);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		shader.stop();
	}
}
