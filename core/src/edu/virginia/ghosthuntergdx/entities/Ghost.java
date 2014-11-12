package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ghost extends Enemies {

	public Ghost(Vector2 position, Texture t, Player player) {
		super(position, t, player);
		
	}
	 public void die(){
		 if (health <= 0){
			 this.remove();
		 }
	 }
		
	@Override 
	public void act(float delta){
		super.act(delta);
		Vector2 playerPos = player.mBody.getPosition();
		Vector2 dir = playerPos.sub(mBody.getPosition());
		dir.nor();
		
		mBody.setLinearVelocity(dir.scl(speed));
	}
}
	
	


