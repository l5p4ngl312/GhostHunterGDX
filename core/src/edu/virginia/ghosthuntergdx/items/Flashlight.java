package edu.virginia.ghosthuntergdx.items;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.items.Weapon.ammoType;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Flashlight extends Weapon{

	public static final float price = 25f;
	private static final int[] fireAnimFrames = {3};
	public static final int index = 2;
	private static final float lightOffset = 0.6f;
	private static final float rightOffset = 0.04f;
	
	public ConeLight playerLight;
	
	public Flashlight(Vector2 worldPos)
	{
		super(price,ammoType.NOAMMO,3,fireAnimFrames,index);
		fireAnimation.setFrameDuration(1/10f);
		setPosition(worldPos.x,worldPos.y);
		cdTime = 0.75f;
		if(!SPGame.debugPhysics)
		{
		playerLight = new ConeLight(SPGame.getRayHandler(), SPGame.raysPerLight, new Color(1,1,1,0.5f), 12, 0, 0,rot,30f);
		playerLight.setStaticLight(false);
		playerLight.setSoft(true);
		playerLight.setSoftnessLength(2f);
		//SPGame.lightsToDeactivate.add(playerLight);
		playerLight.setActive(lightActive);
		}
	}
	
	boolean lightActive = false;
	
	@Override
	public void act(float dt)
	{
		super.act(dt);
		boolean active = lightActive;
		Player p = SPGame.getPlayer();
		Vector2 rightVector = new Vector2(MathUtils.cosDeg(p.rot-90f),MathUtils.sinDeg(p.rot-90f));
		Vector2 vel = p.getBody().getLinearVelocity();
		Vector2 lightPos = p.getBody().getPosition().add(p.getForwardVector().scl(lightOffset)).add(rightVector.scl(rightOffset)).add(vel.scl(0.02f));
		Array<Body> bodies = new Array<Body>();
		SPGame.getPhysicsWorld().getBodies(bodies);
		for(Body b : bodies)
		{
			for(Fixture f : b.getFixtureList())
			{
				if(f.getFilterData().categoryBits == Physics.OBSTACLE)
				{
					if(f.testPoint(lightPos))
					{
						active = false;
						break;
					}
				}
			}
		}
		if(!SPGame.debugPhysics)
		{
		playerLight.setActive(active);
		playerLight.setPosition(lightPos);
		playerLight.setDirection(p.rot);
		}
	}
	
	@Override
	public void OnSlotSwap(Player p)
	{
		super.OnSlotSwap(p);
		if(p.secondaryItem != null)
		{
			if(p.secondaryItem.equals(this))
			{
				//SPGame.lightsToDeactivate.add(playerLight);
				lightActive = false;
				playerLight.setActive(lightActive);
			}
		}
	}
	
	@Override
	public void OnEquip(Player p)
	{
		super.OnEquip(p);
		//SPGame.lightsToActivate.add(playerLight);
		lightActive = true;
		playerLight.setActive(lightActive);
	}
	
	@Override
	public void OnDropped(Player p)
	{
		super.OnDropped(p);
		//SPGame.lightsToDeactivate.add(playerLight);
		lightActive = false;
		playerLight.setActive(lightActive);
	}

}
