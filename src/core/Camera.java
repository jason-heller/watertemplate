package core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import entity.Entity;
import utils.MathUtils;

public class Camera {

	private static int fov = 90;

	public boolean controllable = true;
	
	private Matrix4f projectionMatrix, projectionViewMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();

	private Vector3f position = new Vector3f();
	//private Frustum frustum = new Frustum();

	private float yaw;
	private float pitch;
	private float roll;
	private float angleAroundPlayer, height;
	private static float zoom, targetZoom, zoomSpeed;
	private float shakeTime = 0f, shakeIntensity = 0f;
	private Vector2f screenShake = new Vector2f();
	private Vector3f lookAt = new Vector3f();
	private Vector3f viewDirection;
	
	public float dist = 100;
	
	private Entity focus = null;
	
	private boolean mouseIsGrabbed = false;
	
	public static final float FAR_PLANE = 3000f;
	public static final float NEAR_PLANE = .1f;
	
	public Camera(float x, float y, float z) {
		updateProjection();
		this.position.set(x,y,z);
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void move() {
		dist += 0.02f;
		
		if (Math.abs(targetZoom - zoom) > .2f) {
			zoom += zoomSpeed;
			updateProjection();
		}
		
		if (shakeTime > 0) {
			shakeTime = Math.max(shakeTime-.01f, 0f);
			if (shakeTime == 0) {
				screenShake.zero();
			} else {
				screenShake.set(
						-(shakeIntensity/2f) + (float)(Math.random()*shakeIntensity),
						-(shakeIntensity/2f) + (float)(Math.random()*shakeIntensity));
			}
		}
		
		if (focus != null && controllable) {
			float pitchChange = Mouse.getDY() * .1f;
			float angleChange = Mouse.getDX() * .1f;
			height += pitchChange;
			angleAroundPlayer -= angleChange;
			height = Math.max(-89, Math.min(89, height));
			
			float r = (float)Math.toRadians(angleAroundPlayer);
	
			position.x = (float) (dist * Math.cos(r));
			position.y = (dist/1.5f) + (height);
			position.z = (float) (-dist * Math.sin(r));
			
			position.add(focus.position.x, 0, focus.position.z);
			
			lookAt = Vector3f.sub(position, focus.position).normalize();
			
			pitch = MathUtils.lerp(pitch, (float)Math.toDegrees(Math.asin(lookAt.y)), .05f);
			yaw = -(float)Math.toDegrees(Math.atan2(lookAt.x, lookAt.z));
			
			
		}
		
		updateViewMatrix();
		
	}

	public boolean isShaking() {
		return (shakeTime==0f);
	}
	
	public void grabMouse() {
		Mouse.setGrabbed(true);
	}
	
	public void ungrabMouse() {
		Mouse.setGrabbed(false);
	}

	public boolean isMouseGrabbed() {
		return mouseIsGrabbed;
	}

	public void shake(float time, float intensity) {
		this.shakeIntensity = intensity;
		this.shakeTime = time;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getProjectionViewMatrix() {
		return projectionViewMatrix;
	}

	public void updateViewMatrix() {
		viewMatrix.identity();
		
		Vector2f shake = getScreenShake();
		
		viewMatrix.rotateX(pitch + shake.y);
		viewMatrix.rotateY(yaw + shake.x);
		viewMatrix.rotateZ(roll);
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		viewMatrix.translate(negativeCameraPos);
		
		viewDirection = MathUtils.eulerToVectorDeg(yaw, pitch);
		
		Matrix4f.mul(projectionMatrix, viewMatrix, projectionViewMatrix);
	}
	
	public Vector3f getDirectionVector() {
		return viewDirection;
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}

	private static Matrix4f createProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians((fov-zoom) / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float f) {
		yaw = f;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector2f getScreenShake() {
		return screenShake;
	}

	public void setZoom(float i) {
		targetZoom = i;
		zoomSpeed = (targetZoom - zoom)/45;
		updateProjection();
	}
	
	public void updateProjection() {
		this.projectionMatrix = createProjectionMatrix();
	}

	public void focusOn(Entity focus) {
		this.focus = focus;
		if (focus == null) {
			lookAt = null;
		}
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public float getZoom() {
		// TODO Auto-generated method stub
		return zoom;
	}

}
