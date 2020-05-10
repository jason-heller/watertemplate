package entity;

import utils.ObjLoader;
import utils.TextureLoader;

public class Ground extends Entity {

	
	public Ground() {
		super(ObjLoader.loadObj("res/ground.obj", true), TextureLoader.createTexture("res/ground.png"));
		scale = 10;
		position.y -= 200;
		position.z += 500;
		rotation.x = 5;
	}
	
	public void update() {
		
		
	}
}
