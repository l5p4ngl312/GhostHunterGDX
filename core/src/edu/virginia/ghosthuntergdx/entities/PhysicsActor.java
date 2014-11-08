package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.Consts;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.TextureManager;

public class PhysicsActor extends Actor{

	
	private Sprite sprite;
	private TextureAtlas textureAtlas;
	protected Body mBody;
	public float rot = 0;
	//private int animFrame = 1;
	//private String currentAtlasKey = new String("0001");
	public Vector2 maxVelocity = new Vector2(Float.MAX_VALUE,Float.MAX_VALUE);
	
	public PhysicsActor(Vector2 position, Texture t) {
		super();
		setSprite(new Sprite(t));
		
		setWidth(t.getWidth()/Consts.PIXEL_TO_METER);
		setHeight(t.getHeight()/Consts.PIXEL_TO_METER);
		setOrigin(getWidth()/2,getHeight()/2);
		
		setPosition(position.x,position.y);
		getSprite().setSize(getWidth()*Consts.BOX_TO_WORLD, getHeight()*Consts.BOX_TO_WORLD);
		getSprite().setOriginCenter();
		getSprite().setPosition(position.x*Consts.BOX_TO_WORLD, position.y*Consts.BOX_TO_WORLD);
		
		FixtureDef mDef = new FixtureDef();
		mBody = Physics.createCircleBody(BodyType.DynamicBody, mDef, getSprite());
		
	}


	@Override
	public void act(float delta) {
		super.act(delta);
		setPosition(mBody.getPosition().x,mBody.getPosition().y);
		
	}

	@Override
	public void draw(Batch batch,float parentAlpha) {
		super.draw(batch, parentAlpha);
		getSprite().setPosition(getX()*Consts.BOX_TO_WORLD-getWidth()/2*Consts.BOX_TO_WORLD,getY()*Consts.BOX_TO_WORLD-getHeight()/2*Consts.BOX_TO_WORLD);
		getSprite().setRotation(rot);
		getSprite().draw(batch, parentAlpha);
		
	}

	public Body getBody()
	{
		return mBody;
	}


	public Sprite getSprite() {
		return sprite;
	}


	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}


	

}
