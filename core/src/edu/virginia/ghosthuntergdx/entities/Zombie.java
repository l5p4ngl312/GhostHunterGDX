package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.Collider;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Bullet;
import edu.virginia.ghosthuntergdx.screens.SPGame;


public class Zombie extends Enemy implements Collider {

	
	float speed = 1f;

	private float linearDamping = 5.0f;
	public static TextureRegion idle;
	private Animation attack;
	private Animation hit;
	private float attackSpeed = 2.5f;
	private float hitSpeed = 5f;
	
	private int[] attackFrames = {2,3,1};
	private int[] hitFrames = {0,1};
	
	public Zombie(Vector2 position, Player player) {
		super(position, idle, idle.getRegionWidth(),(idle.getRegionHeight()/2));
		getSprite().setSize(idle.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, idle.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/2,getSprite().getHeight()-getSprite().getHeight()/4);
		
		mBody.setLinearDamping(linearDamping);
		
		health = 30;
		Array<TextureRegion> animRegions = new Array<TextureRegion>();
		for(int i = 0; i < attackFrames.length; i++)
			animRegions.add(TextureManager.zombie.getRegions().get(attackFrames[i]));
		attack = new Animation(1/attackSpeed,animRegions);
		
		animRegions = new Array<TextureRegion>();
		for(int i = 0; i < hitFrames.length; i++)
			animRegions.add(TextureManager.zombie.getRegions().get(hitFrames[i]));
		hit = new Animation(1/hitSpeed,animRegions);
	}
	 public void checkDeath(){
		 if (health <= 0){
			 this.remove();
			 SPGame.destroyBody(this.mBody);
		 }
	 }
	 
	private final float attackCooldown = 1.4f;
	private float attackTimer = attackCooldown;
	@Override 
	public void act(float delta){
		super.act(delta);
		attackTimer += delta;
		
		checkDeath();
		//moving the zombie towards the player
		Vector2 playerPos = SPGame.getPlayer()
				.mBody.getPosition();
		Vector2 dir = playerPos.sub(mBody.getPosition());
		
		float dist = dir.len();
		if(dist<3){
			
		dir.nor();
		moveDir = dir;
		lookAtTarget(delta);
		
		if(dist <= 1.1f)
		{
			attack();
		}else{
			mBody.setLinearVelocity(dir.scl(speed));
		}
		
		
		}else{
			moveDir = Vector2.Zero;
			super.act(delta);
		}
		
		checkAttackHit();
	}
	
	private void lookAtTarget(float delta)
	{
		//Set the enemies target rotation based on either his move direction 
		targetRot = getSprite().getRotation();
		if(moveDir.len() > 0)
		{
			targetRot = (float) Math.atan2(moveDir.y,moveDir.x)*MathUtils.radiansToDegrees + angleOffset;

		}
		//Rotate the enemy towards his target rotation along the shortest path
		if(targetRot > 360)
			targetRot-=360;
		if(rot > 360)
			rot-=360;
		
		if(targetRot < 0)
			targetRot+=360;
		if(rot < 0)
			rot+=360;
		
		if(Math.abs(targetRot - rot) > 180)
		{
			if(rot < targetRot)
				rot+=360;
			else
				targetRot+=360;
		}
		
		
		if(Math.abs(targetRot - rot) > 0)
		{
			if(rot < targetRot)
			{
				rot+=rotSpeed*delta;
				if(rot > targetRot)
					rot = targetRot;
			}else
			{
				rot-=rotSpeed*delta;
				if(rot < targetRot)
					rot = targetRot;
			}
		}
	}
	
	private void attack()
	{
		if(attackTimer > attackCooldown)
		{
			currentAnim = attack;
			animTime = 0;
			attackTimer = 0;
			SoundManager.zombieAttack.play(0.3f);
			didDamage = false;
		}
	}
	
	boolean didDamage = false;
	private void checkAttackHit()
	{
		if(currentAnim.equals(attack))
		{
			if(animTime > 0.6f)
			{
				if((SPGame.getPlayer().mBody.getPosition().sub(mBody.getPosition())).len() <= 1.1f && !didDamage)
				{
				SPGame.getPlayer().Hurt(damage);
				didDamage = true;
				}
			}
		}
	}
	
	@Override
	public void OnCollisionBegin(Body other, Contact c) {
		if ( other.getUserData()!=null){
			if(other.getUserData() instanceof Bullet){
				Bullet b = (Bullet) other.getUserData();
				this.health = health - b.damage;
				SoundManager.zombieHit.play(0.3f);
			}
		}		
	}
		

	
	@Override
	public void OnCollisionEnd(Body other, Contact c) {
		
	}
}
	
	


