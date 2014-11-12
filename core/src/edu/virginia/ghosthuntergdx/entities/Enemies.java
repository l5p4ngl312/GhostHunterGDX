package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemies extends PhysicsActor{

	
	Vector2 position;
	Texture enemyTexture;
	Rectangle bounds;
	Player player;
	double health;
	float speed = 2f;
	
	
	public Enemies(Vector2 position, Texture t, Player player) {
		super(position, t);
		this.position = position;
		this.player = player;
	}
	
	public void act(float delta){
		super.act(delta);
		
		
	}
	
}
	

