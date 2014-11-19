package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Flashlight;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Ghost extends Enemy {


	private float linearDamping = 5.0f;
	private float alphaFadeSpeed = 0.1f;
	public float targetAlpha = 0.0f;
	
	public Ghost(Vector2 position, Player player) {
		super(position, TextureManager.ghostR,TextureManager.ghostR.getRegionWidth(),TextureManager.ghostR.getRegionHeight());
		getSprite().setSize(TextureManager.ghostR.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, TextureManager.ghostR.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/2,getSprite().getHeight()/2);
		mBody.setLinearDamping(linearDamping);
		Filter ghostFilter = new Filter();
		ghostFilter.categoryBits = Physics.GHOST;
		ghostFilter.groupIndex = Physics.NO_GROUP;
		ghostFilter.maskBits = Physics.MASK_GHOST;
		mBody.getFixtureList().get(0).setFilterData(ghostFilter);
		getSprite().setColor(new Color(1,1,1,targetAlpha));
		}
	
	@Override
	public void act(float delta){
	super.act(delta);
	
		for(Actor a : SPGame.getHUDStage().getActors())
		{
			if(a instanceof Flashlight)
			{
				Flashlight f = (Flashlight)a;
				if(f.playerLight.isActive())
				{
					if(f.playerLight.contains(getX(), getY()))
					{
						Gdx.app.debug("GHOST IN LIGHT", "Ghost in light");
						targetAlpha = 0.5f;
					}else{
						targetAlpha = 0.0f;
					}
				}
			}
		}
	}
	
	@Override
	public void draw(Batch batch,float parentAlpha)
	{
		float myAlpha = getSprite().getColor().a;
		getSprite().setAlpha(MathUtils.lerp(myAlpha, targetAlpha, alphaFadeSpeed));
		super.draw(batch, parentAlpha);
	}

}
