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
	
	
	
	public Enemies(Vector2 position, Texture t, Player player) {
		super(position, t);
		this.position = position;
		this.player = player;
	}
	
	public void update(){
		
	
		if(player.mBody.getPosition().x > position.x){
		position.x++;
	}
		else{
		position.x--;
	}	
		if(player.mBody.getPosition().y < position.y){
		position.y++;
	}
		else{
		position.y--;
		}
		
	}
	
}
	

