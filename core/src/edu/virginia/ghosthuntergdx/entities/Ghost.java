package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.Collider;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Artifact;
import edu.virginia.ghosthuntergdx.items.Bullet;
import edu.virginia.ghosthuntergdx.items.Flashlight;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Ghost extends Enemy{


	private float linearDamping = 5.0f;
	private float alphaFadeSpeed = 0.1f;
	public float targetAlpha = 0.0f;
	public static TextureRegion idle;
	
	private Animation attack;
	private Animation hit;
	private float attackSpeed = 2.5f;
	private float hitSpeed = 3.5f;
	
	private int[] attackFrames = {2,3,1};
	private int[] hitFrames = {0,1};
	public Artifact associatedArtifact;
	
	float speed = 2.35f;
	
	
	
	public Ghost(Vector2 position) {
		super(position, idle,idle.getRegionWidth(),idle.getRegionHeight()/2);
		getSprite().setSize(idle.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, idle.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/2,getSprite().getHeight()-getSprite().getHeight()/4);
		mBody.setLinearDamping(linearDamping);
		Filter ghostFilter = new Filter();
		ghostFilter.categoryBits = Physics.GHOST;
		ghostFilter.groupIndex = Physics.NO_GROUP;
		ghostFilter.maskBits = Physics.MASK_GHOST;
		mBody.getFixtureList().get(0).setFilterData(ghostFilter);
		getSprite().setColor(new Color(1,1,1,targetAlpha));
		
		health = 20;
		
		Array<TextureRegion> animRegions = new Array<TextureRegion>();
		for(int i = 0; i < attackFrames.length; i++)
			animRegions.add(TextureManager.ghost.getRegions().get(attackFrames[i]));
		attack = new Animation(1/attackSpeed,animRegions);
		
		animRegions = new Array<TextureRegion>();
		for(int i = 0; i < hitFrames.length; i++)
			animRegions.add(TextureManager.ghost.getRegions().get(hitFrames[i]));
		hit = new Animation(1/hitSpeed,animRegions);
		
		}
	
	private float lastAlpha = 0.0f;
	private float alertTimer = 10f;
	private float alertTime = 10f;
	
	//Ghost only appears when in the light of the player's flashlight. If the ghost is illuminated when it has not recently been seen, an alert sound is played
	boolean frozen = false;
	@Override
	public void act(float delta){

	Vector2 playerPos = SPGame.getPlayer().mBody.getPosition();
	Vector2 toPlayer = playerPos.sub(mBody.getPosition());	
		
		
	alertTimer += delta;
	boolean foundLight = false;
		for(Actor a : SPGame.getHUDStage().getActors())
		{
			if(a instanceof Flashlight)
			{
				Flashlight f = (Flashlight)a;
				if(f.lightActive)
				{
					foundLight = true;
					if(f.playerLight.contains(getX(), getY()))
					{
						targetAlpha = 0.5f;
						frozen = true;
					}else{
						targetAlpha = 0.0f;
						frozen = false;
					}
				}
			}
		}
		if(!foundLight)
		{
			targetAlpha = 0.0f;
			frozen = false;
		}
		if(lastAlpha == 0.0f && targetAlpha != 0.0f && alertTimer > alertTime)
		{
			SoundManager.ghostAlert.play(0.4f);
			alertTimer = 0;
		}
		
		lastAlpha = targetAlpha;
		attackTimer += delta;
		
		if(!frozen)
		{
			super.act(delta);
			if(attackTimer > attackTime)
			{
				playerPos = SPGame.getPlayer().mBody.getPosition();
				toPlayer = playerPos.sub(mBody.getPosition());
				moveDir = toPlayer;
				lookAtTarget(delta);
				if(toPlayer.len() <= 2)
				{
					targetAlpha = 0.5f;
				}else{
					targetAlpha = 0.0f;
				}
				
				if(toPlayer.len() <= 1.1f)
				{
					attack();
				}else{
					Vector2 dir = moveDir.nor();
					mBody.setLinearVelocity(dir.scl(speed));
				}
			}else if(!attacking){
				moveTimer += delta;
				if(moveTimer > moveTime)
				{
					moveTime = (float)Math.random()*6f;
					getNewMoveDir();
				}
				lookAtTarget(delta);
				Vector2 dir = moveDir.nor();
				mBody.setLinearVelocity(dir.scl(speed));
			}
			checkAttackHit();
			if(attacking)
			{
				targetAlpha = 0.5f;
			}
		}else{
			if(toPlayer.len() < 2.5f && !associatedArtifact.detected)
			{
				associatedArtifact.detected = true;
				SPGame.displayString = "Artifact detected!";
				SPGame.displayKillMessage = true;
				SPGame.getPlayer().setArtifactsFound(SPGame.getPlayer().artifactsFound+1);
			}
		}
	}
	
	float attackTimer = 0;
	public float attackTime = 10f;
	
	float moveTime = (float)Math.random()*6f;
	float moveTimer = 0;
	private void getNewMoveDir()
	{
		moveTimer = 0;
		if(!moveDir.equals(Vector2.Zero))
		{
			moveDir = Vector2.Zero;
			moveTime = (float)Math.random()*3f;
		}else{
			moveDir = new Vector2((float)Math.random()*2-1,(float)Math.random()*2-1);
		}
	}
	boolean attacking = false;
	private void attack()
	{
			currentAnim = attack;
			animTime = 0;
			attackTimer = 0;
			didDamage = false;
			attacking = true;
	}
	
	boolean didDamage = false;
	private void checkAttackHit()
	{
		if(currentAnim != null)
		{
		if(currentAnim.equals(attack))
		{
			if(animTime > 0.45f)
			{
				if((SPGame.getPlayer().mBody.getPosition().sub(mBody.getPosition())).len() <= 1.1f && !didDamage)
				{
				SPGame.getPlayer().Hurt(damage);
				didDamage = true;
				}
			}
			if(animTime > 0.8f)
			{
				attacking = false;
				targetAlpha = 0.0f;
			}
		}
		}
	}
	
	public void kill(){
			 this.remove();
			 
			 double gKills = SPGame.getPlayer().getGhostKills();
			 SPGame.getPlayer().setGhostKills(gKills + 1);
			 
			 double kills = SPGame.getPlayer().getKills();
			 SPGame.getPlayer().setKills(kills + 1);
			 
			 
			 SPGame.destroyBody(this.mBody);
			 SPGame.displayString = "Ghost killed!";
			 SPGame.displayKillMessage = true;
			 SPGame.getDirector().playerProgressed();
			 
	 }
	
	@Override
	public void draw(Batch batch,float parentAlpha)
	{
		float myAlpha = getSprite().getColor().a;
		getSprite().setAlpha(MathUtils.lerp(myAlpha, targetAlpha, alphaFadeSpeed));
		super.draw(batch, parentAlpha);
	}

	
	

}
