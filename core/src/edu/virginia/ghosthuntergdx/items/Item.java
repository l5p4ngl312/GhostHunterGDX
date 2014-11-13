package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.ContactListener;

import edu.virginia.ghosthuntergdx.Consts;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.SPGame;
import edu.virginia.ghosthuntergdx.TextureManager;
import edu.virginia.ghosthuntergdx.entities.Player;

public abstract class Item extends Actor{

	public float price = 0;
	public Sprite icon;
	public Sprite pickUp;
	
	public boolean isPickedUp = false;
	public Body pickUpBody;
	
	public static final float itemPixelToWorld = 115f;
	public static final float secondaryIconScale = 0.5f;
	public static final float primaryIconScale = 1.5f;
	
	public float rot = 0;
	
	public Item(int i)
	{
		icon = new Sprite(TextureManager.items.getRegions().get(i));
		pickUp = new Sprite(TextureManager.items.getRegions().get(i+1));
		setWidth(icon.getWidth()/itemPixelToWorld);
		setHeight(icon.getHeight()/itemPixelToWorld);
		
		icon.setSize(getWidth()*Consts.BOX_TO_WORLD, getHeight()*Consts.BOX_TO_WORLD);
		pickUp.setSize(getWidth()*Consts.BOX_TO_WORLD, getHeight()*Consts.BOX_TO_WORLD);
		icon.setOrigin(icon.getWidth(),icon.getHeight());
		pickUp.setOrigin(pickUp.getWidth(),pickUp.getHeight());
		
		//Create a physics body based on the sprite
		FixtureDef mDef = new FixtureDef();
		mDef.filter.categoryBits = Physics.PICKUP;
		mDef.filter.groupIndex = Physics.NO_GROUP;
		mDef.filter.maskBits = Physics.MASK_PICKUP;
		mDef.isSensor = true;
		pickUpBody = Physics.createCircleBody(BodyType.DynamicBody, mDef, pickUp,false);
		pickUpBody.setTransform(new Vector2(getX(),getY()), rot);
		pickUpBody.setUserData(this);
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		pickUpBody.setTransform(new Vector2(x,y), rot);
	}
	
	@Override
	public void act(float delta)
	{
		if(!isPickedUp)
		{
		super.setPosition(pickUpBody.getPosition().x,pickUpBody.getPosition().y);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if(!isPickedUp)
		{
			pickUp.setPosition(getX()*Consts.BOX_TO_WORLD-pickUp.getOriginX(), getY()*Consts.BOX_TO_WORLD-pickUp.getOriginY());
			pickUp.setRotation(rot);
			pickUp.draw(batch,parentAlpha);
		}else{
			icon.setPosition(getX()-icon.getOriginX(), getY()-icon.getOriginY());
			icon.setRotation(rot);
			icon.draw(batch,parentAlpha);
		}
	}
	
	public void OnPickedUp(Player p)
	{
		isPickedUp = true;
		this.remove();
		SPGame.destroyBody(pickUpBody);
		SPGame.getHUDStage().addActor(this);
		if(p.primaryItem != null)
		{
			if(p.primaryItem.equals(this))
			{
				setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()-20);
				icon.setScale(primaryIconScale);
			}
		}
		if(p.secondaryItem != null)
		{
			if(p.secondaryItem.equals(this))
			{
				setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()-20);
				icon.setScale(secondaryIconScale);
			}
		}
	}
	
}

