package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends PhysicsActor{

	
	Vector2 position;
	Rectangle bounds;
	Player player;
	double health;
	float speed;
	
	
	public Enemy(Vector2 position, TextureRegion t, Player player) {
		super(position, new TextureRegion(t));
		this.position = position;
		this.player = player;
	}
	
	

	


	public void act(float delta){
		super.act(delta);
		
		
	}
	
}
	

