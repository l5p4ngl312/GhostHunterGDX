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
	
	public void update(){
		
		Vector2 playerPos = player.mBody.getPosition();
		Vector2 dir = playerPos.sub(mBody.getPosition());
		dir.nor();
		
		mBody.setLinearVelocity(dir.scl(speed));
		
	}
	
}
	

