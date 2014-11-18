package edu.virginia.ghosthuntergdx.items;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Pistol extends Weapon{

	
	public static final float price = 75f;
	private static final int[] fireAnimFrames = {1,0};
	public static final int index = 0;
	private static final float lightOffset = 1.5f;
	private static final float bulletOffset = 1.15f;
	
	private static final float maxLightLifeTime = 0.075f;
	private static final float shakeTime = 0.2f;
	
	private float recoilVelocity = 0.9f;
	private float bulletVelocity = 20f;
	
	public Pistol(Vector2 worldPos, int ammoInClip)
	{
		super(price,ammoType.PISTOL,index,fireAnimFrames,index);
		fireAnimation.setFrameDuration(1/10f);
		setPosition(worldPos.x,worldPos.y);
		cdTime = 0.75f;
		clipSize = 9;
		this.ammoInClip = ammoInClip;
	}

	PointLight pistolFlash;
	float lightLifeTime = 0;
	@Override
	public void fire(Vector2 attackDir,Player p) {
		super.fire(attackDir,p);
		if(this.fired)
		{
			p.getBody().setLinearVelocity(p.getBody().getLinearVelocity().sub(p.getForwardVector().scl(recoilVelocity)));
			SoundManager.pistolShot.play(0.8f);
			if(!SPGame.debugPhysics)
			{
				pistolFlash = new PointLight(SPGame.getRayHandler(),SPGame.raysPerLight,new Color(1.0f,1.0f,0.0f,0.3f), 1.75f,p.getX()+p.getForwardVector().scl(lightOffset).x,p.getY()+p.getForwardVector().scl(lightOffset).y);
				pistolFlash.setStaticLight(true);
				pistolFlash.setSoft(true);
				pistolFlash.setSoftnessLength(0.75f);
			}
			Bullet shot = new Bullet(p.getBody().getPosition().add(attackDir.scl(bulletOffset)),attackDir.scl(bulletVelocity));
			lightLifeTime = 0;
			SPGame.screenShake = true;
			flashing = true;
		}
	}

	boolean flashing = false;
	@Override
	public void act(float dt)
	{
		super.act(dt);
			lightLifeTime += dt;
			if(flashing)
			{
				if(lightLifeTime > maxLightLifeTime)
				{
					if(!SPGame.debugPhysics)
					{
						pistolFlash.remove();
					}
				flashing = false;
				}
			}
			if(lightLifeTime > shakeTime && SPGame.screenShake)
			{
			SPGame.screenShake = false;
			}
	}
}
