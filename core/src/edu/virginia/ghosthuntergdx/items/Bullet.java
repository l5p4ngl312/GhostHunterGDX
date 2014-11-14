package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import edu.virginia.ghosthuntergdx.Collider;
public class Bullet extends Actor implements Collider{

	public float damage = 10;
	public Body mBody;
	public Sprite bulletSprite;
	private Animation bulletAnim;
	
	public Bullet(Vector2 pos, Vector2 velocity)
	{
		
	}

	@Override
	public void OnCollisionBegin(Body other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnCollisionEnd(Body other) {
		// TODO Auto-generated method stub
		
	}
}
