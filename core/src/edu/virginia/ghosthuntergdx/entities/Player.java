package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import edu.virginia.ghosthuntergdx.Consts;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.SPGame;
import edu.virginia.ghosthuntergdx.TextureManager;

public class Player extends PhysicsActor{

	Vector2 moveDir = new Vector2(0,0);
	Vector2 attackDir = new Vector2(0,0);
	
	public final float baseMoveSpeed = 3f;
	public float moveSpeed = baseMoveSpeed;
	public float rotSpeed = 900f;
	
	public static TextureRegion holdingPistol;
	public static TextureRegion idleFists;
	
	public Player(Vector2 position) {
		super(position, idleFists,Physics.PLAYER,Physics.NO_GROUP,Physics.MASK_PLAYER,idleFists.getRegionWidth()/2,idleFists.getRegionHeight(),true);
		
		//Set the sprite's size to the objects size converted to screen coordinates
		getSprite().setSize(idleFists.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, idleFists.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/4-getSprite().getWidth()/8,getSprite().getHeight()/2);
		maxVelocity = new Vector2(5,5);
		spriteOffset = new Vector2(-width/4,0);
	}
	public Player(Vector2 position,TextureRegion t) {
		super(position, t,Physics.PLAYER,Physics.NO_GROUP,Physics.MASK_PLAYER,idleFists.getRegionWidth()/2,idleFists.getRegionHeight(),true);
		//Set the sprite's size to the objects size converted to screen coordinates
		getSprite().setSize(t.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, idleFists.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/4-getSprite().getWidth()/8,getSprite().getHeight()/2);
		maxVelocity = new Vector2(5,5);
		spriteOffset = new Vector2(-width/4,0);
	}
	float width = idleFists.getRegionWidth()/Consts.PIXEL_TO_METER/2;

	@Override
	public void act(float delta)
	{
		super.act(delta);
		movePlayer(delta);
	}

	private void movePlayer(float delta)
	{
		//If player is attacking, slow his movement speed
		if(!attackDir.equals(Vector2.Zero))
		{
			moveSpeed = baseMoveSpeed/1.5f;
		}else{
			moveSpeed = baseMoveSpeed;
		}
		
		//Set the players velocity to the direction of the move stick times the player's speed
		mBody.setLinearVelocity(moveDir.x*moveSpeed,moveDir.y*moveSpeed);
		
		//Set the players target rotation based on either his move direction or attack stick direction
		float targetRot = getSprite().getRotation();
		if(moveDir.len() > 0 && attackDir.equals(Vector2.Zero))
		{
			targetRot = (float) Math.atan2(moveDir.y,moveDir.x)*MathUtils.radiansToDegrees;

		}else if(!attackDir.equals(Vector2.Zero)){
			targetRot = (float) Math.atan2(attackDir.y,attackDir.x)*MathUtils.radiansToDegrees;
		}
		//Rotate the player towards his target rotation along the shortest path
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

		//Rotate body about sprite origin
		float aimedAngle = rot*MathUtils.degRad;
		while(aimedAngle < -MathUtils.PI) aimedAngle += MathUtils.PI*2;
		while(aimedAngle > MathUtils.PI) aimedAngle -= MathUtils.PI*2;
		
		mBody.setAwake(true);
		Vector2 beforeWorldPos = new Vector2(mBody.getWorldPoint(new Vector2(-width/4,0)));
		Gdx.app.debug("BEFORE", beforeWorldPos.toString());
		mBody.setTransform(mBody.getPosition(), aimedAngle);
		Vector2 afterWorldPos = new Vector2(mBody.getWorldPoint(new Vector2(-width/4,0)));
		Gdx.app.debug("AFTER", afterWorldPos.toString());
		mBody.setTransform(mBody.getPosition().add(beforeWorldPos.sub(afterWorldPos)), aimedAngle);
		
		
	}
	
	
	public void setMoveDir(Vector2 target)
	{
		moveDir = target;
	}
	
	public void setAttackDir(Vector2 target)
	{
		attackDir = target;
	}

}
