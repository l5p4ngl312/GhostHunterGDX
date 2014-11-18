package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;

public class Ghost extends Enemy {


	private float linearDamping = 5.0f;
	
	public Ghost(Vector2 position, Player player) {
		super(position, TextureManager.ghostR, player);
		getSprite().setSize(TextureManager.ghostR.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, TextureManager.ghostR.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/4-getSprite().getWidth()/8,getSprite().getHeight()/2);
		mBody.setLinearDamping(linearDamping);
		
		}
	

	public void act(float delta){
	super.act(delta);
	
	
	
	}

}
