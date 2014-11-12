package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ghost extends Enemies {

	public Ghost(Vector2 position, Texture t, Player player) {
		super(position, t, player);
		
	}
	
	
	@Override 
	public void act(float delta){
		super.act(delta);
		update(delta);
	}
	
	

}
