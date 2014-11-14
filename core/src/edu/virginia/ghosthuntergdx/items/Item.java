package edu.virginia.ghosthuntergdx.items;

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

import edu.virginia.ghosthuntergdx.GameInputListener;
import edu.virginia.ghosthuntergdx.GameInputListener.TouchInfo;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public abstract class Item extends Actor{

	public float price = 0;
	public Sprite icon;
	public Sprite pickUp;
	
	public boolean isPickedUp = false;
	public Body pickUpBody;
	
	public static final float itemPixelToWorld = 115f;
	public static final float secondaryIconScale = 0.9f;
	public static final float primaryIconScale = 1.5f;
	
	public float rot = 0;
	
	public float dropVelocity = 2f;
	
	
	public float friction = 0.8f;
	public float angularFriction = 1f;
	
	public Item(int i)
	{
		icon = new Sprite(TextureManager.items.getRegions().get(i));
		pickUp = new Sprite(TextureManager.items.getRegions().get(i+1));
		setWidth(icon.getWidth()/itemPixelToWorld);
		setHeight(icon.getHeight()/itemPixelToWorld);
		
		icon.setSize(getWidth()*Consts.BOX_TO_WORLD, getHeight()*Consts.BOX_TO_WORLD);
		pickUp.setSize(getWidth()*Consts.BOX_TO_WORLD, getHeight()*Consts.BOX_TO_WORLD);
		icon.setOrigin(icon.getWidth(),icon.getHeight());
		pickUp.setOrigin(pickUp.getWidth()/2,pickUp.getHeight()/2);
		
		//Create a physics body based on the sprite
		FixtureDef mDef = new FixtureDef();
		mDef.filter.categoryBits = Physics.PICKUP;
		mDef.filter.groupIndex = Physics.NO_GROUP;
		mDef.filter.maskBits = Physics.MASK_PICKUP;

		FixtureDef sDef = new FixtureDef();
		sDef.filter.categoryBits = Physics.SENSOR;
		sDef.filter.groupIndex = Physics.NO_GROUP;
		sDef.filter.maskBits = Physics.MASK_SENSOR;
		sDef.isSensor = true;

		pickUpBody = Physics.createCircleBody(BodyType.DynamicBody, mDef,sDef, pickUp,false);
		pickUpBody.setTransform(new Vector2(getX(),getY()), rot);
		pickUpBody.setUserData(this);
		pickUpBody.setLinearDamping(friction);
		pickUpBody.setAngularDamping(angularFriction);
		}
	
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		if(!isPickedUp)
		{
		pickUpBody.setTransform(new Vector2(x,y),pickUpBody.getAngle());
		}
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		if(!isPickedUp)
		{
			super.setPosition(pickUpBody.getPosition().x,pickUpBody.getPosition().y);
		}
		
		if(dropDisabled <= dropDisableTime)
		{
			dropDisabled+=delta;
		}

	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if(!isPickedUp)
		{
			pickUp.setPosition(getX()*Consts.BOX_TO_WORLD-pickUp.getOriginX(), getY()*Consts.BOX_TO_WORLD-pickUp.getOriginY());
			pickUp.setRotation(pickUpBody.getAngle()*MathUtils.radDeg);
			pickUp.draw(batch,parentAlpha);
		}else{
			icon.setPosition(getX()-icon.getOriginX(), getY()-icon.getOriginY());
			icon.setRotation(rot);
			icon.draw(batch,parentAlpha);
		}
	}
	
	public void OnPickedUp(Player p)
	{
		Gdx.app.debug("PICKUP", "Item picked up");
		isPickedUp = true;
		this.remove();
		SPGame.bodiesToDeactivate.add(pickUpBody);
		SPGame.getHUDStage().addActor(this);
		OnSlotSwap(p);
	}
	
	public void OnSlotSwap(Player p)
	{
		if(p.primaryItem == null)
		{
			p.setIdleFrame(p.idleFrame);
		}else{
			if(p.primaryItem instanceof Weapon)
			{
				Weapon w = (Weapon)p.primaryItem;
				w.OnEquip(p);
			}
		}
		
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
				setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()-icon.getHeight()-20);
				icon.setScale(secondaryIconScale);
			}
		}
	}
	
	public static final float dropDisableTime = 0.25f;
	public float dropDisabled=dropDisableTime;
	public void OnDropped(Player p)
	{
		if(p.primaryItem != null)
		{
			if(p.primaryItem.equals(this))
			{
			p.setIdleFrame(p.idleFrame);
			p.primaryItem = null;
			}
		}
		this.remove();
		SPGame.getPickUpGroup().addActor(this);
		isPickedUp = false;
		Vector2 dropOffset = p.getForwardVector().scl(0.4f);
		SPGame.bodiesToActivate.add(pickUpBody);
		setPosition(p.getX()+dropOffset.x,p.getY()+dropOffset.y);
		pickUpBody.setLinearVelocity(p.getForwardVector().scl(dropVelocity));
		float spin = 7.5f;
		pickUpBody.setAngularVelocity((float)Math.random()*spin*2-spin);
		dropDisabled=0;
	}
}

