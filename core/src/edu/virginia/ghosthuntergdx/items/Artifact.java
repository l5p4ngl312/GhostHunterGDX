/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.Collider;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.entities.Ghost;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Artifact extends Actor implements Collider {

	public Ghost associatedGhost;
	Body mBody;
	Sprite sprite;

	public float health = 5;
	public boolean detected = false;

	public Artifact(Vector2 pos) {
		setSprite(new Sprite(TextureManager.artifact));

		setWidth(TextureManager.artifact.getWidth() / Consts.PIXEL_TO_METER);
		setHeight(TextureManager.artifact.getHeight() / Consts.PIXEL_TO_METER);
		setOrigin(getWidth() / 2, getHeight() / 2);

		setPosition(pos.x, pos.y);

		// Set the sprite's size to the objects size converted to screen
		// coordinates
		getSprite().setSize(getWidth() * Consts.BOX_TO_WORLD,
				getHeight() * Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getWidth() * Consts.BOX_TO_WORLD / 2,
				getHeight() * Consts.BOX_TO_WORLD / 2);
		getSprite().setPosition(pos.x * Consts.BOX_TO_WORLD,
				pos.y * Consts.BOX_TO_WORLD);

		// Create a physics body based on the sprite
		FixtureDef mDef = new FixtureDef();
		mDef.filter.categoryBits = Physics.ARTIFACT;
		mDef.filter.groupIndex = 0;
		mDef.filter.maskBits = Physics.MASK_ARTIFACT;
		mBody = Physics.createCircleBody(BodyType.StaticBody, mDef,
				getSprite(), false);
		mBody.setUserData(this);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		getSprite().setPosition(
				getX() * Consts.BOX_TO_WORLD - getSprite().getOriginX(),
				getY() * Consts.BOX_TO_WORLD - getSprite().getOriginY());
		getSprite().draw(batch, parentAlpha);
		if (detected) {
			getSprite().setColor(Color.RED);
		} else {
			getSprite().setColor(Color.WHITE);
		}
	}

	float despawnTimer = 0;
	float despawnTime = 20f;
	boolean destroyed = false;

	@Override
	public void act(float delta) {
		super.act(delta);
		// Set the actor's position to the physics body's position
		Vector2 pos = mBody.getPosition();
		setPosition(pos.x, pos.y);

		if (health <= 0) {
			despawnTimer += delta;
			getSprite().setRegion(TextureManager.destroyedArtifact);
			Vector2 p = SPGame.getPlayer().getBody().getPosition();
			Vector2 dist = p.sub(mBody.getPosition());
			if (dist.len() > 8 && despawnTimer > despawnTime) {
				this.remove();
			}
		}
	}

	@Override
	public void OnCollisionBegin(Body other, Contact c) {
		if (other.getUserData() != null) {
			if (other.getUserData() instanceof Bullet) {
				Bullet b = (Bullet) other.getUserData();
				this.health = health - b.damage;
				if (health <= 0 && !destroyed) {
					detected = false;
					destroyed = true;
					if (associatedGhost != null) {
						associatedGhost.kill();
					}
				}
			}
		}

	}

	@Override
	public void OnCollisionEnd(Body other, Contact c) {
		// TODO Auto-generated method stub

	}

	public Body getBody() {
		return mBody;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
