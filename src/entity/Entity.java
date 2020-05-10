package entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import utils.Model;

public class Entity {
	public Vector3f position, rotation;
	public Model model;
	public int texture;
	public float scale = 1f;
	
	public Entity(Model m, int t) {
		model = m;
		texture = t;
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
	public void update() {
		
	}
	
	public Matrix4f getMatrix() {
		Matrix4f matrix = new Matrix4f();// Identity
		
		matrix.translate(position.x, position.y, position.z);
		matrix.rotateX(rotation.x);
		matrix.rotateY(rotation.y);
		matrix.rotateZ(rotation.z);
		matrix.scale(scale);
		
		return matrix;
	}
}
