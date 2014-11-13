package edu.virginia.ghosthuntergdx.items;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import edu.virginia.ghosthuntergdx.SPGame;
import edu.virginia.ghosthuntergdx.entities.Player;

public class Pistol extends Weapon{

	
	public static final float price = 75f;
	private static final int[] fireAnimFrames = {1,0};
	public static final int index = 0;
	private static final float lightOffset = 1.5f;
	
	private static final float maxLightLifeTime = 0.075f;
	
	public Pistol(Vector2 worldPos)
	{
		super(price,ammoType.PISTOL,0,fireAnimFrames,index);
		fireAnimation.setFrameDuration(1/10f);
		setPosition(worldPos.x,worldPos.y);
		
	}

	PointLight pistolFlash;
	float lightLifeTime = 0;
	@Override
	public void fire(Vector2 attackDir,Player p) {
		super.fire(attackDir,p);
		if(this.fired)
		{
			pistolFlash = new PointLight(SPGame.getRayHandler(),SPGame.raysPerLight,new Color(1.0f,1.0f,0.0f,0.5f), 2.25f,p.getX()+p.getForwardVector().scl(lightOffset).x,p.getY()+p.getForwardVector().scl(lightOffset).y);
			pistolFlash.setStaticLight(true);
			pistolFlash.setSoft(true);
			pistolFlash.setSoftnessLength(0.75f);
			lightLifeTime = 0;
			flashing = true;
		}
	}

	boolean flashing = false;
	@Override
	public void act(float dt)
	{
		super.act(dt);
		if(flashing)
		{
			lightLifeTime += dt;
			if(lightLifeTime > maxLightLifeTime)
			{
				pistolFlash.remove();
				flashing = false;
			}
		}
	}
}
