package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Flashlight;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Ghost extends Enemy {


	private float linearDamping = 5.0f;
	private float alphaFadeSpeed = 0.1f;
	public float targetAlpha = 0.0f;
	public static TextureRegion idle;
	
	private Animation attack;
	private Animation hit;
	private float attackSpeed = 0.8f;
	private float hitSpeed = 2f;
	
	private int[] attackFrames = {2,3,1};
	private int[] hitFrames = {0,1};
	
	
	public Ghost(Vector2 position, Player player) {
		super(position, idle,idle.getRegionWidth(),idle.getRegionHeight()/2);
		getSprite().setSize(idle.getRegionWidth()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD, idle.getRegionHeight()/Consts.PIXEL_TO_METER*Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth()/2,getSprite().getHeight()-getSprite().getHeight()/4);
		mBody.setLinearDamping(linearDamping);
		Filter ghostFilter = new Filter();
		ghostFilter.categoryBits = Physics.GHOST;
		ghostFilter.groupIndex = Physics.NO_GROUP;
		ghostFilter.maskBits = Physics.MASK_GHOST;
		mBody.getFixtureList().get(0).setFilterData(ghostFilter);
		getSprite().setColor(new Color(1,1,1,targetAlpha));
		
		Array<TextureRegion> animRegions = new Array<TextureRegion>();
		for(int i = 0; i < attackFrames.length; i++)
			animRegions.add(TextureManager.zombie.getRegions().get(attackFrames[i]));
		attack = new Animation(1/attackSpeed,animRegions);
		
		animRegions = new Array<TextureRegion>();
		for(int i = 0; i < hitFrames.length; i++)
			animRegions.add(TextureManager.zombie.getRegions().get(hitFrames[i]));
		hit = new Animation(1/hitSpeed,animRegions);
		
		}
	
	private float lastAlpha = 0.0f;
	private float alertTimer = 10f;
	private float alertTime = 10f;
	
	//Ghost only appears when in the light of the player's flashlight. If the ghost is illuminated when it has not recently been seen, an alert sound is played
	@Override
	public void act(float delta){
	super.act(delta);
	
	alertTimer += delta;
		for(Actor a : SPGame.getHUDStage().getActors())
		{
			boolean foundLight = false;
			if(a instanceof Flashlight)
			{
				Flashlight f = (Flashlight)a;
				if(f.playerLight.isActive())
				{
					foundLight = true;
					if(f.playerLight.contains(getX(), getY()))
					{
						targetAlpha = 0.5f;
					}else{
						targetAlpha = 0.0f;
					}
				}
			}
			if(!foundLight)
				targetAlpha = 0.0f;
		}
		if(lastAlpha == 0.0f && targetAlpha != 0.0f && alertTimer > alertTime)
		{
			SoundManager.ghostAlert.play(0.4f);
			alertTimer = 0;
		}
		
		lastAlpha = targetAlpha;
	}
	
	@Override
	public void draw(Batch batch,float parentAlpha)
	{
		float myAlpha = getSprite().getColor().a;
		getSprite().setAlpha(MathUtils.lerp(myAlpha, targetAlpha, alphaFadeSpeed));
		super.draw(batch, parentAlpha);
	}

}
