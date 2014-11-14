package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.entities.Player;

public abstract class Weapon extends Item{

	public enum ammoType{PISTOL,SHOTGUN,AR,BOMB,NOAMMO};
	public int playerHoldingFrame;
	public int[] fireAnimFrames;
	
	public float fireAnimSpeed = 2f;
	
	public ammoType myAmmoType;
	
	public int bulletsPerShot = 1;
	public float fireAngle = 0;
	public float accuracy = 1.0f;
	public float cdTime = 0.75f;
	
	private float elapsedTime = cdTime;
	
	public Animation fireAnimation;
	
	public Weapon(float price,ammoType t, int playerHoldingFrame, int[] fireAnimFrames, int spriteIndex) {
		super(spriteIndex);
		this.playerHoldingFrame = playerHoldingFrame;
		this.fireAnimFrames = fireAnimFrames;
		myAmmoType = t;
		Array<TextureRegion> animRegions = new Array<TextureRegion>();
		for(int i = 0; i < fireAnimFrames.length; i++)
			animRegions.add(TextureManager.player.getRegions().get(fireAnimFrames[i]));
		
		fireAnimation = new Animation(1/fireAnimSpeed,animRegions);
	}

	protected boolean fired = false;
	public void fire(Vector2 attackDir,Player p)
	{
		fired = false;
		if(elapsedTime > cdTime)
		{
			fired = true;
			elapsedTime = 0;
			p.setAnimTime(0);
			//Play fire sound
			
		}
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		elapsedTime+=delta;
	}
	
	@Override
	public void OnPickedUp(Player p)
	{
		super.OnPickedUp(p);
		if(p.primaryItem != null)
		{
			if(p.primaryItem.equals(this))
			{
				OnEquip(p);
			}
		}
	}
	
	public void OnEquip(Player p)
	{
		p.setIdleFrame(playerHoldingFrame);
		p.setAttackAnim(fireAnimation, fireAnimSpeed);
	}
}
