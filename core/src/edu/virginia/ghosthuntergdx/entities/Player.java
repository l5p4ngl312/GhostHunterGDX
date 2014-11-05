package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;

import edu.virginia.ghosthuntergdx.Consts;
import edu.virginia.ghosthuntergdx.TextureManager;

public class Player extends PhysicsEntity{

	Vector2 moveTarget = new Vector2(0,0);
	public float moveSpeed = 3f;
	public float rotSpeed = 1f;
	
	public Player(Vector2 position) {
		super(position, TextureManager.player);
		maxVelocity = new Vector2(5,5);
	}
	public Player(Vector2 position,Texture t) {
		super(position, t);
	}
	
	@Override
	public void update()
	{
		super.update();
		Vector2 moveDir = moveTarget.sub(position);
		//Gdx.app.debug("POS", position.toString());
		moveDir.nor();
		if(mBody.getLinearVelocity().len() < maxVelocity.len() )
		{
			mBody.setLinearVelocity(moveDir.x*moveSpeed,moveDir.y*moveSpeed);
		}
		
		if(moveDir.len() > 0)
		{
		float targetRot = (float) Math.atan2(moveDir.y,moveDir.x)*MathUtils.radiansToDegrees;

		rot = targetRot;
		}
		
		moveTarget = mBody.getPosition().scl(Consts.BOX_TO_WORLD);
	}
	
	
	public void setMoveTarget(Vector2 target)
	{
		moveTarget = target;
		Gdx.app.debug("TARGET", moveTarget.toString());
	}

}
