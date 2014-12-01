package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Enemy extends PhysicsActor{

	
	Vector2 position;
	Rectangle bounds;
	double health = 100;
	float speed;
	float damage = 10;
	float targetRot;
	Vector2 moveDir = new Vector2(0,0);
	public float angleOffset = 90;
	public float rotSpeed = 900f;
	
	public Animation currentAnim;
	public float animTime = 1000f;
	
	public Enemy(Vector2 position, TextureRegion t, float bodyWidth, float bodyHeight) {
		super(position, new TextureRegion(t),Physics.ENEMYBODY,Physics.NO_GROUP,Physics.MASK_ENEMYBODY,bodyWidth,bodyHeight,false);
		this.position = position;
		
	}
	


	public void act(float delta){
		super.act(delta);
		
		if(currentAnim != null)
		{
			getSprite().setRegion(currentAnim.getKeyFrame(animTime,false));
			animTime += delta;
		}
		
	}
	
}
	

