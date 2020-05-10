package skybox;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import core.Camera;
import utils.Model;
import utils.TextureLoader;

// This handles the logic between the player's character and the game
// This is basically just copy-pasted out of my game engine, so sorry if it seems a lil weird
public class Skybox {
	private static final float SIZE = 32;

	public static final float DAY_LENGTH = 300;
	
	private static final float CYCLE_INTEVAL = DAY_LENGTH / 4f;
	private final SkyboxShader shader;
	private final Model box;
	private float alpha;

	private final Vector3f topColor = new Vector3f(), bottomColor = new Vector3f();

	private final Vector3f[] SKY_COLOR = new Vector3f[] {
			// Morning
			new Vector3f(255 / 255f, 153 / 255f, 153 / 255f), new Vector3f(255 / 255f, 51 / 255f, 153 / 255f),
			// Day
			new Vector3f(102 / 255f, 178 / 255f, 255 / 255f), new Vector3f(0 / 255f, 102 / 255f, 104 / 255f),
			// Evening
			new Vector3f(255 / 255f, 178 / 255f, 102 / 255f), new Vector3f(255 / 255f, 0 / 255f, 127 / 255f),
			// Night
			new Vector3f(51 / 255f, 0 / 255f, 102 / 255f), new Vector3f(25 / 255f, 0 / 255f, 51 / 255f),
			// Copy of morning
			new Vector3f(255 / 255f, 153 / 255f, 153 / 255f), new Vector3f(255 / 255f, 51 / 255f, 153 / 255f) };

	private final float[] BG_ALPHAS = new float[] { .25f, 0f, .5f, 1f, .25f };

	private final int starTexture;

	private float rotation;

	public Skybox() {
		this.shader = new SkyboxShader();
		this.box = CubeGenerator.generateCube(SIZE);
		/*
		 * skyboxTexture = new CubemapTexture(new String[] { "sky/stars.png",
		 * "sky/stars.png", "sky/stars.png", "sky/stars.png", "sky/stars.png",
		 * "sky/stars.png"
		 * 
		 * });
		 */
		starTexture = TextureLoader.createTexture("res/sky/stars.png", true);
		GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}

	/**
	 * Delete the shader when the game closes.
	 */
	public void cleanUp() {
		GL11.glDeleteTextures(starTexture);
		shader.cleanUp();
	}

	private void getColors(float time) {
		int gameTime = (int) (time % DAY_LENGTH);
		final int currentCycle = (int) (gameTime / CYCLE_INTEVAL) * 2;
		final float cycleDuration = gameTime % CYCLE_INTEVAL / CYCLE_INTEVAL;

		alpha = lerp(BG_ALPHAS[currentCycle / 2], BG_ALPHAS[currentCycle / 2 + 1], cycleDuration);
		topColor.set(Vector3f.lerp(SKY_COLOR[currentCycle + 2], SKY_COLOR[currentCycle], cycleDuration));
		bottomColor.set(Vector3f.lerp(SKY_COLOR[currentCycle + 3], SKY_COLOR[currentCycle + 1], cycleDuration));
	}

	// Linear interpolation helper function
	private float lerp(float s, float t, float amount) {
		return s * (1f - amount) + t * amount;
	}

	private void prepare(Camera camera) {
		shader.start();
		final Matrix4f matrix = new Matrix4f(camera.getViewMatrix());
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		// matrix.rotateX(camera.getPitch());
		matrix.rotateY(rotation);
		rotation += .01f;
		shader.projectionMatrix.loadMatrix(camera.getProjectionMatrix());
		shader.viewMatrix.loadMatrix(matrix);
		/*
		 * OpenGlUtils.disableBlending(); OpenGlUtils.enableDepthTesting(true);
		 * OpenGlUtils.cullBackFaces(true); OpenGlUtils.antialias(false);
		 */
	}

	public void render(Camera camera, float time) {
		prepare(camera);
		box.bind(0);
		getColors(time);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, starTexture);
		shader.bgAlpha.loadFloat(alpha);
		shader.topColor.loadVec3(topColor);
		shader.bottomColor.loadVec3(bottomColor);
		GL11.glDrawElements(GL11.GL_TRIANGLES, box.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		box.unbind(0);
		shader.stop();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

}
