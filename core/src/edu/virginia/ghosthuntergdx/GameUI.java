package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Item;

public class GameUI extends Actor{

	Sprite primarySlot;
	Sprite secondarySlot;
	
	public GameUI()
	{
		primarySlot = new Sprite(TextureManager.itemSlot);
		secondarySlot = new Sprite(TextureManager.itemSlot);
		
		primarySlot.setSize(primarySlot.getWidth()/Item.itemPixelToWorld*Consts.BOX_TO_WORLD, primarySlot.getHeight()/Item.itemPixelToWorld*Consts.BOX_TO_WORLD);
		secondarySlot.setSize(primarySlot.getWidth(), primarySlot.getHeight());
		
		primarySlot.setOrigin(primarySlot.getWidth(),primarySlot.getHeight());
		secondarySlot.setOrigin(secondarySlot.getWidth(),secondarySlot.getHeight());
		
		primarySlot.setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/15-primarySlot.getOriginX()+5,Gdx.graphics.getHeight()-20-primarySlot.getOriginY()+5);
		secondarySlot.setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/30-secondarySlot.getOriginX()+5,Gdx.graphics.getHeight()-secondarySlot.getHeight()-20-secondarySlot.getOriginY()-5);
		
		primarySlot.setScale(Item.primaryIconScale+0.05f);
		secondarySlot.setScale(Item.secondaryIconScale+0.05f);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		primarySlot.draw(batch,parentAlpha);
		secondarySlot.draw(batch,parentAlpha);
	}
}
