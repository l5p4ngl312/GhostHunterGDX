package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;


public class Zombie extends Enemy {

	
	float speed = 1f;

	private float linearDamping = 5.0f;
	
	public Zombie(Vector2 position, Player player) {
		super(position, TextureManager.zombieR, player);
		getSprite().setSize(TextureManager.zombieR.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, TextureManager.zombieR.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/4-getSprite().getWidth()/8,getSprite().getHeight()/2);
		
		mBody.setLinearDamping(linearDamping);
		
	}
	 public void die(){
		 if (health <= 0){
			 this.remove();
		 }
	 }
		
	@Override 
	public void act(float delta){
		super.act(delta);
		
		//moving the zombie towards the player
		Vector2 playerPos = player.mBody.getPosition();
		Vector2 dir = playerPos.sub(mBody.getPosition());
		if(dir.len()<3){
		dir.nor();
		
		mBody.setLinearVelocity(dir.scl(speed));
		}
	}
}
	
	


