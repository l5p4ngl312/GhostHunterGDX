package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.items.Weapon.ammoType;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Ammo extends Item{

	public static final int index = 4;
	
	public ammoType myType;
	public int count = 18;
	
	
	public Ammo(Vector2 worldPos, ammoType t) {
		super(index);
		myType = t;
		setPosition(worldPos.x,worldPos.y);
		if(t== ammoType.SHOTGUN)
		{
			pickUp.setRegion(TextureManager.items.getRegions().get(6));
		}else if(t== ammoType.AR)
		{
			pickUp.setRegion(TextureManager.items.getRegions().get(8));
		}else if(t== ammoType.BOMB)
		{
			pickUp.setRegion(TextureManager.items.getRegions().get(10));
		}else if(t== ammoType.PISTOL)
		{
			pickUp.setRegion(TextureManager.items.getRegions().get(4));
		}
	}
	
	@Override
	public void OnPickedUp(Player p)
	{
		if(myType== ammoType.SHOTGUN)
		{
			p.ammoCount[1] += count;
		}else if(myType== ammoType.AR)
		{
			p.ammoCount[2] += count;
		}else if(myType== ammoType.BOMB)
		{
			p.ammoCount[3] += count;
		}else if(myType== ammoType.PISTOL)
		{
			p.ammoCount[0] += count;
		}
		
		if(p.primaryItem != null)
		{
			if(p.primaryItem instanceof Weapon)
			{
				Weapon w = (Weapon)p.primaryItem;
				SPGame.getUI().clipCount = w.ammoInClip;
				SPGame.getUI().reserveCount = p.getAmmoCount(w.myAmmoType);
			}
		}
		this.remove();
		SPGame.bodiesToDeactivate.add(pickUpBody);
		SoundManager.pickup.play(0.3f);
	}

}
