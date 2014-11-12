package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;

import edu.virginia.ghosthuntergdx.Consts;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.TextureManager;

public class Player extends PhysicsActor{

	Vector2 moveDir = new Vector2(0,0);
	Vector2 attackDir = new Vector2(0,0);
	
	public final float baseMoveSpeed = 3f;
	public float moveSpeed = baseMoveSpeed;
	public float rotSpeed = 900f;
	
	public Player(Vector2 position) {
		super(position, TextureManager.player,Physics.PLAYER,Physics.NO_GROUP,Physics.MASK_PLAYER,TextureManager.player.getWidth()/2,TextureManager.player.getHeight());
		maxVelocity = new Vector2(5,5);
	}
	public Player(Vector2 position,Texture t) {
		super(position, t,Physics.PLAYER,Physics.NO_GROUP,Physics.MASK_PLAYER,TextureManager.player.getWidth()/2,TextureManager.player.getHeight());
	}
	
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
