package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import edu.virginia.ghosthuntergdx.Consts;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.TextureManager;

public class PhysicsEntity extends Entity{

	
	private Sprite sprite;
	private TextureAtlas textureAtlas;
	protected Body mBody;
	public float rot = 0;
	//private int animFrame = 1;
	//private String currentAtlasKey = new String("0001");
	public Vector2 maxVelocity = new Vector2(Float.MAX_VALUE,Float.MAX_VALUE);
	
	public PhysicsEntity(Vector2 position, Texture t) {
		super(position);
		sprite = new Sprite(t);
		scale = new Vector2(1f,1f);
		sprite.setSize(scale.x*Consts.BOX_TO_WORLD, scale.y*Consts.BOX_TO_WORLD);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(position.x, position.y);
		
		FixtureDef mDef = new FixtureDef();
		mBody = Physics.createBoxBody(BodyType.DynamicBody, mDef, sprite);
	}


	@Override
	public void update() {
		
		position = mBody.getPosition().scl(Consts.BOX_TO_WORLD);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.setPosition(position.x,position.y);
		sprite.setRotation(rot);
		sprite.draw(batch);
		
	}


	@Override
	public void dispose() {
		
	}
	
	public Body getBody()
	{
		return mBody;
	}


	

}
