/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 */

package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import edu.virginia.ghosthuntergdx.Collider;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Bullet extends Actor implements Collider {

	public float damage = 10;
	public Body mBody;
	public Sprite bulletSprite;

	private static final float colorRate = 0.08f;

	public Bullet(Vector2 pos, Vector2 velocity, float damage) {
		this.damage = damage;
		SPGame.getProjectileGroup().addActor(this);
		Texture t = TextureManager.bullet;
		bulletSprite = new Sprite(t);
		setWidth(t.getWidth() / Consts.PIXEL_TO_METER);
		setHeight(t.getHeight() / Consts.PIXEL_TO_METER);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setPosition(pos.x, pos.y);

		bulletSprite.setSize(getWidth() * Consts.BOX_TO_WORLD, getHeight()
				* Consts.BOX_TO_WORLD);
		bulletSprite.setOrigin(getWidth() * Consts.BOX_TO_WORLD / 2,
				getHeight() * Consts.BOX_TO_WORLD / 2);
		bulletSprite.setPosition(pos.x * Consts.BOX_TO_WORLD, pos.y
				* Consts.BOX_TO_WORLD);
		bulletSprite.setColor(1, 1, 1, 1);
		FixtureDef mDef = new FixtureDef();
		mDef.filter.categoryBits = Physics.SENSOR;
		mDef.filter.groupIndex = Physics.NO_GROUP;
		mDef.filter.maskBits = Physics.MASK_SENSOR;

		final BodyDef circleBodyDef = new BodyDef();
		circleBodyDef.type = BodyType.DynamicBody;
		circleBodyDef.bullet = true;

		circleBodyDef.position.x = getX();
		circleBodyDef.position.y = getY();

		final CircleShape shape = new CircleShape();
		shape.setRadius(0.1f);

		mDef.shape = shape;
		mDef.density = 1;
		mBody = SPGame.getPhysicsWorld().createBody(circleBodyDef);
		mBody.createFixture(mDef);
		mBody.setUserData(this);
		mBody.setLinearVelocity(velocity);
		rot = MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radDeg;
		bulletSprite.setRotation(rot);
	}

	private float rot;

	@Override
	public void act(float dt) {
		super.act(dt);
		if (!kill)
			setPosition(mBody.getPosition().x, mBody.getPosition().y);
		else
			this.remove();
	}

	@Override
	public void draw(Batch b, float parentAlpha) {
		super.draw(b, parentAlpha);
		bulletSprite.setPosition(
				getX() * Consts.BOX_TO_WORLD - bulletSprite.getOriginX(),
				getY() * Consts.BOX_TO_WORLD - bulletSprite.getOriginY());
		Vector3 myColor = new Vector3(bulletSprite.getColor().r,
				bulletSprite.getColor().g, bulletSprite.getColor().b);
		myColor.lerp(new Vector3(0.9f, 0.9f, 0), colorRate);
		bulletSprite.setColor(myColor.x, myColor.y, myColor.z, 1);
		bulletSprite.draw(b, parentAlpha);
	}

	boolean kill = false;

	@Override
	public void OnCollisionBegin(Body other, Contact c) {
		kill = true;
		SPGame.destroyBody(mBody);
		if (other.getFixtureList().get(0).getFilterData().categoryBits == Physics.OBSTACLE) {
			SPGame.getProjectileGroup().addActor(
					new BulletImpact(c.getWorldManifold().getPoints()[0],
							(float) Math.atan2(-mBody.getLinearVelocity().y,
									-mBody.getLinearVelocity().x)
									* MathUtils.radDeg));
		}
	}

	@Override
	public void OnCollisionEnd(Body other, Contact c) {
		// TODO Auto-generated method stub

	}
}
