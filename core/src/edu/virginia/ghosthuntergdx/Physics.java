package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Physics {

	public static Body createBoxBody( final BodyType pBodyType, final FixtureDef pFixtureDef, Sprite pSprite ) {

	    float pRotation = 0;
	    float pWidth = pSprite.getWidth();
	    float pHeight = pSprite.getHeight();

	    final BodyDef boxBodyDef = new BodyDef();
	    boxBodyDef.type = pBodyType;

	    boxBodyDef.position.x = pSprite.getX() / Consts.BOX_TO_WORLD;
	    boxBodyDef.position.y = pSprite.getY() / Consts.BOX_TO_WORLD;

	    // Temporary Box shape of the Body
	    final PolygonShape boxPoly = new PolygonShape();
	    final float halfWidth = pWidth * 0.5f / Consts.BOX_TO_WORLD;
	    final float halfHeight = pHeight * 0.5f / Consts.BOX_TO_WORLD;
	    boxPoly.setAsBox( halfWidth, halfHeight );  // set the anchor point to be the center of the sprite

	    pFixtureDef.shape = boxPoly;        

	    final Body boxBody = SPGame.getPhysicsWorld().createBody(boxBodyDef);
	    boxBody.createFixture(pFixtureDef);
	    
	    return boxBody;
	}
}
