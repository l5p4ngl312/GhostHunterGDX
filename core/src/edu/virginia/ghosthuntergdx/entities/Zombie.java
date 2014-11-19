package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.virginia.ghosthuntergdx.Collider;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Bullet;
import edu.virginia.ghosthuntergdx.screens.SPGame;


public class Zombie extends Enemy implements Collider {

	
	float speed = 1f;

	private float linearDamping = 5.0f;
	
	public Zombie(Vector2 position, Player player) {
		super(position, TextureManager.zombieR, TextureManager.zombieR.getRegionWidth(),(TextureManager.zombieR.getRegionHeight()/2));
		getSprite().setSize(TextureManager.zombieR.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, TextureManager.zombieR.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/2,getSprite().getHeight()-getSprite().getHeight()/4);
		
		mBody.setLinearDamping(linearDamping);
		
		health = 10;
	}
	 public void die(){
		 if (health <= 0){
			 this.remove();
			 SPGame.destroyBody(this.mBody);
		 }
	 }
	 
	
		
	@Override 
	public void act(float delta){
		super.act(delta);
		
		
		die();
		
		//moving the zombie towards the player
		Vector2 playerPos = SPGame.getPlayer()
				.mBody.getPosition();
		Vector2 dir = playerPos.sub(mBody.getPosition());
		if(dir.len()<3){
		dir.nor();
		
		mBody.setLinearVelocity(dir.scl(speed));
		}
	}
	@Override
	public void OnCollisionBegin(Body other, Contact c) {
		if ( other.getUserData()!=null){
			if(other.getUserData() instanceof Bullet){
				Bullet b = (Bullet) other.getUserData();
				this.health = health - b.damage;
			}
		}		
	}
		

	
	@Override
	public void OnCollisionEnd(Body other, Contact c) {
		
	}
}
	
	


