package entity;

import utils.ObjLoader;
import utils.TextureLoader;

public class Boat extends Entity {

	private float timer = 0f;
	
	public Boat() {
		super(ObjLoader.loadObj("res/boat.obj", true), TextureLoader.createTexture("res/boat.png"));
	}
	
	public void update() {
		timer += 0.02f;
		
		float bob = (float)Math.sin(timer);
		
		position.y = 50 + (5 * bob);
		position.z = (-2 * bob) + timer * 40;
		rotation.x = 8 * bob;
		
		
	}
}
