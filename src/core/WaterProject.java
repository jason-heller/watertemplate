package core;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import entity.Boat;
import entity.EntityRenderer;
import entity.Ground;
import entity.Prof;
import opengl.fbo.FboUtils;
import opengl.fbo.FrameBuffer;
import skybox.Skybox;
import utils.MathUtils;
import water.WaterRenderer;

public class WaterProject {
	
	private static final String TITLE = "BIG BOAT ADVENTURES";
	
	private WaterRenderer waterRenderer;
	private Skybox skybox;
	private Camera camera;
	private Boat boat;
	private Prof prof;
	private Ground ground;
	
	private float timer;
	
	private FrameBuffer reflectionFbo, refractionFbo;
	
	public WaterProject() {
		try {
			Display.create(new PixelFormat(),
					new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Display.setTitle(TITLE);
		Display.setInitialBackground(1, 1, 1);
		Display.setVSyncEnabled(false);
		
		skybox = new Skybox();
		
		waterRenderer = new WaterRenderer();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glCullFace(GL11.GL_BACK);
		
		camera = new Camera(0,24,0);
		
		reflectionFbo = FboUtils.createTextureFbo(640, 360);
		refractionFbo = FboUtils.createTextureFbo(640, 360);
		
		EntityRenderer.init();
		
		boat = new Boat();
		boat.position.set(32, 0, -32);
		prof = new Prof();
		ground = new Ground();
		
		camera.focusOn(boat);
		
		update();
		
		cleanUp();
	}

	private void update() {
		while(!Display.isCloseRequested()) {
			timer += .1f;
			boat.update();
			prof.update();
			camera.move();
			renderWater();
			
			if (boat.position.z > 500) {
				camera.focusOn(null);
				camera.controllable = false;
				camera.setYaw(MathUtils.angleLerp(camera.getYaw(), 180, .005f));
				camera.setPitch(MathUtils.angleLerp(camera.getPitch(), -30, .005f));
				camera.setPosition(new Vector3f(camera.getPosition().x,
						MathUtils.lerp(camera.getPosition().y, 50, .005f),
						MathUtils.lerp(camera.getPosition().z, 400, .005f)));
			}
			
			if (boat.position.z > 1111 && boat.position.z < 1300) {
				camera.shake(1, 2);
				boat.scale = Math.max(boat.scale-.1f, 0);
			}
			
			if (boat.position.z > 1300) {
				prof.scale = Math.max(prof.scale-.1f, 0);
			}
			
			Display.sync(60);
			Display.update();
		}
	}

	private void renderWater() {
		renderRefractions(camera);
		renderReflections(camera);

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		
		renderScene(0, 1, 0, 100000);
		waterRenderer.render(camera, reflectionFbo, refractionFbo, timer);
	}

	private void renderScene(float x, float y, float z, float w) {
		skybox.render(camera, timer);
		
		Vector3f light = new Vector3f();
		light.z = (float) Math.cos(timer * MathUtils.TAU / Skybox.DAY_LENGTH);
		light.y = (float) Math.sin(timer * MathUtils.TAU / Skybox.DAY_LENGTH);
		
		EntityRenderer.render(camera, light, boat, new Vector4f(x,y,z,w));
		EntityRenderer.render(camera, light, prof, new Vector4f(x,y,z,w));
		EntityRenderer.render(camera, light, ground, new Vector4f(x,y,z,w));
		
	}

	private void cleanUp() {
		waterRenderer.cleanUp();
		skybox.cleanUp();
		EntityRenderer.cleanUp();
		Display.destroy();
	}
	
	private void renderRefractions(Camera camera) {
		refractionFbo.bind();
		renderScene(0,-1,0,0);
		
		refractionFbo.unbind();
	}

	private void renderReflections(Camera camera) {
		float pitch = camera.getPitch();
		float offset = (camera.getPosition().y)*2;
		
		
		reflectionFbo.bind();
		camera.setPitch(-pitch);
		camera.getPosition().y -= offset;
		camera.updateViewMatrix();
		
		renderScene(0,1,0,0);
		
		reflectionFbo.unbind();
		camera.setPitch(pitch);
		camera.getPosition().y += offset;
		camera.updateViewMatrix();
	}

	public static void main(String[] args) {
		new WaterProject();
	}
}
