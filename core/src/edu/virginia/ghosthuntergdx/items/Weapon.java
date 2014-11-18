package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public abstract class Weapon extends Item{

	public enum ammoType{PISTOL,SHOTGUN,AR,BOMB,NOAMMO};
	public int playerHoldingFrame;
	public int[] fireAnimFrames;
	
	public float fireAnimSpeed = 2f;
	
	public ammoType myAmmoType;
	
	public float cdTime = 0.75f;
	public int clipSize = 9;
	
	private float elapsedTime = cdTime;
	
	public Animation fireAnimation;
	
	public int ammoInClip = 0;
	private boolean reloading = false;
	
	public float reloadTime = 1.0f;
	
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
		
		if(reloading && elapsedTime > reloadTime)
		{
			reloading = false;
		}
		
		if(elapsedTime > cdTime && ammoInClip > 0)
		{
			
			if(!reloading)
			{
			fired = true;
			elapsedTime = 0;
			p.setAnimTime(0);
			ammoInClip--;
			SPGame.getUI().clipCount = ammoInClip;
			}
			
		}else if(elapsedTime > cdTime && !(myAmmoType == ammoType.NOAMMO))
		{
			reload(p);
		}
	}
	
	public void reload(Player p)
	{
		reloading = true;
		elapsedTime = 0;
		if(p.getAmmoCount(myAmmoType) > 0)
		{
			if(p.getAmmoCount(myAmmoType) >= clipSize)
			{
				p.useAmmo(myAmmoType, clipSize);
				ammoInClip = clipSize;
			}else{
				int ammo = p.getAmmoCount(myAmmoType);
				p.useAmmo(myAmmoType,ammo);
				ammoInClip = ammo;
			}
			SPGame.getUI().clipCount = ammoInClip;
			SPGame.getUI().reserveCount = p.getAmmoCount(myAmmoType);
		}else{
			SoundManager.pickup.play(0.3f);
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
		if(p.primaryItem == null)
		{
			p.primaryItem = this;
			super.OnPickedUp(p);
			OnEquip(p);
		}else if(p.secondaryItem == null)
		{
			p.secondaryItem = this;
			super.OnPickedUp(p);
		}
		
	}
	
	public void OnEquip(Player p)
	{
		p.setIdleFrame(playerHoldingFrame);
		p.setAttackAnim(fireAnimation, fireAnimSpeed);
		SPGame.getUI().clipCount = ammoInClip;
		SPGame.getUI().reserveCount = p.getAmmoCount(myAmmoType);
		SPGame.getUI().setAmmoIcon(myAmmoType);
	}
	
	public void OnDropped(Player p)
	{
		super.OnDropped(p);
		SPGame.getUI().setAmmoIcon(ammoType.NOAMMO);
	}
}
