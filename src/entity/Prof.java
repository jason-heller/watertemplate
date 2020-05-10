package entity;

import utils.ObjLoader;
import utils.TextureLoader;

public class Prof extends Entity {

	
	public Prof() {
		super(ObjLoader.loadObj("res/prof.obj", true), TextureLoader.createTexture("res/prof.png"));
		position.set(30,40,1002);
		rotation.y = 180;
		scale = 3;
	}
	
	public void update() {
		
		
	}
}
