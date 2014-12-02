/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 */

package edu.virginia.ghosthuntergdx.entities;

import java.util.ArrayList;

import box2dLight.ConeLight;
import box2dLight.Light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Item;
import edu.virginia.ghosthuntergdx.items.Weapon;
import edu.virginia.ghosthuntergdx.items.Weapon.ammoType;
import edu.virginia.ghosthuntergdx.screens.GameOver;
import edu.virginia.ghosthuntergdx.screens.MainMenu;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Player extends PhysicsActor {

	Vector2 moveDir = new Vector2(0, 0);
	Vector2 attackDir = new Vector2(0, 0);

	public final float baseMoveSpeed = 3f;
	public float moveSpeed = baseMoveSpeed;
	public float rotSpeed = 900f;

	public static TextureRegion idleFists;
	public static final int idleFrame = 2;

	public Item primaryItem;
	public Item secondaryItem;
	public ArrayList<Item> Inventory = new ArrayList<Item>();

	private Animation attackAnim;
	private float animSpeed;
	private float animTime = 1000f;

	private float linearDamping = 5.0f;

	private long stepSoundID;

	public int[] ammoCount = new int[3];

	public double kills = 0, zombieKills = 0, ghostKills = 0, shotsFired = 0,
			artifactsFound = 0;

	private float hurtRumbleTime = 0.2f;
	private float hurtRumbleTimer = hurtRumbleTime;
	private boolean rumbling = false;

	public Player(Vector2 position) {
		super(position, idleFists, Physics.PLAYER, Physics.NO_GROUP,
				Physics.MASK_PLAYER, idleFists.getRegionWidth() / 2, idleFists
						.getRegionHeight(), true);

		// Set the sprite's size to the objects size converted to screen
		// coordinates
		getSprite().setSize(
				idleFists.getRegionWidth() / Consts.PIXEL_TO_METER
						* Consts.BOX_TO_WORLD,
				idleFists.getRegionHeight() / Consts.PIXEL_TO_METER
						* Consts.BOX_TO_WORLD);
		getSprite().setOrigin(
				getSprite().getWidth() / 4 - getSprite().getWidth() / 8,
				getSprite().getHeight() / 2);
		maxVelocity = new Vector2(5, 5);
		spriteOffset = new Vector2(-width / 4, 0);
		mBody.setLinearDamping(linearDamping);
	}

	public Player(Vector2 position, TextureRegion t) {
		super(position, t, Physics.PLAYER, Physics.NO_GROUP,
				Physics.MASK_PLAYER, idleFists.getRegionWidth() / 2, idleFists
						.getRegionHeight(), true);
		// Set the sprite's size to the objects size converted to screen
		// coordinates
		getSprite().setSize(
				t.getRegionWidth() / Consts.PIXEL_TO_METER
						* Consts.BOX_TO_WORLD,
				idleFists.getRegionHeight() / Consts.PIXEL_TO_METER
						* Consts.BOX_TO_WORLD);
		getSprite().setOrigin(
				getSprite().getWidth() / 4 - getSprite().getWidth() / 8,
				getSprite().getHeight() / 2);
		maxVelocity = new Vector2(5, 5);
		spriteOffset = new Vector2(-width / 4, 0);
		mBody.setLinearDamping(linearDamping);
	}

	float width = idleFists.getRegionWidth() / Consts.PIXEL_TO_METER / 2;

	@Override
	public void act(float delta) {
		super.act(delta);
		hitIndicatorTimer += delta;
		movePlayer(delta);
		attackLogic(delta);

		hurtRumbleTimer += delta;
		if (hurtRumbleTimer > hurtRumbleTime && rumbling) {
			SPGame.screenShake = false;
			rumbling = false;
		}
	}

	private float targetRot = rot;

	private float TIME_BETWEEN_STEPS = 0.75f / baseMoveSpeed;
	private float timeToNextStep = 0;

	private void movePlayer(float delta) {
		// If player is attacking, slow his movement speed
		if (!attackDir.equals(Vector2.Zero)) {
			moveSpeed = baseMoveSpeed / 2.5f;

		} else {
			moveSpeed = baseMoveSpeed;
		}
		TIME_BETWEEN_STEPS = 0.7f / (new Vector2(moveDir.x * moveSpeed,
				moveDir.y * moveSpeed)).len();
		// Set the players velocity to the direction of the move stick times the
		// player's speed
		if (!moveDir.equals(moveDir.Zero)) {
			mBody.setLinearVelocity(moveDir.x * moveSpeed, moveDir.y
					* moveSpeed);

			timeToNextStep -= delta;
			if (timeToNextStep <= 0) {
				stepSoundID = SoundManager.footstep.play(0.1f);
				timeToNextStep = TIME_BETWEEN_STEPS;
			}
		} else {
			timeToNextStep = 0.1f;
		}

		// Set the players target rotation based on either his move direction or
		// attack stick direction
		targetRot = getSprite().getRotation();
		if (moveDir.len() > 0 && attackDir.equals(Vector2.Zero)) {
			targetRot = (float) Math.atan2(moveDir.y, moveDir.x)
					* MathUtils.radiansToDegrees;

		} else if (!attackDir.equals(Vector2.Zero)) {
			targetRot = (float) Math.atan2(attackDir.y, attackDir.x)
					* MathUtils.radiansToDegrees;
		}
		// Rotate the player towards his target rotation along the shortest path
		if (targetRot > 360)
			targetRot -= 360;
		if (rot > 360)
			rot -= 360;

		if (targetRot < 0)
			targetRot += 360;
		if (rot < 0)
			rot += 360;

		if (Math.abs(targetRot - rot) > 180) {
			if (rot < targetRot)
				rot += 360;
			else
				targetRot += 360;
		}

		if (Math.abs(targetRot - rot) > 0) {
			if (rot < targetRot) {
				rot += rotSpeed * delta;
				if (rot > targetRot)
					rot = targetRot;
			} else {
				rot -= rotSpeed * delta;
				if (rot < targetRot)
					rot = targetRot;
			}
		}

		// Rotate body about sprite origin
		float aimedAngle = rot * MathUtils.degRad;
		while (aimedAngle < -MathUtils.PI)
			aimedAngle += MathUtils.PI * 2;
		while (aimedAngle > MathUtils.PI)
			aimedAngle -= MathUtils.PI * 2;

		mBody.setAwake(true);
		Vector2 beforeWorldPos = new Vector2(mBody.getWorldPoint(new Vector2(
				-width / 4, 0)));
		mBody.setTransform(mBody.getPosition(), aimedAngle);
		Vector2 afterWorldPos = new Vector2(mBody.getWorldPoint(new Vector2(
				-width / 4, 0)));
		mBody.setTransform(
				mBody.getPosition().add(beforeWorldPos.sub(afterWorldPos)),
				aimedAngle);
	}

	private void attackLogic(float delta) {
		if (attackAnim != null && primaryItem != null) {
			animTime += delta;
			getSprite().setRegion(attackAnim.getKeyFrame(animTime, false));
		}

		if (!attackDir.equals(Vector2.Zero) && rot == targetRot) {
			if (primaryItem instanceof Weapon) {
				Weapon primWeapon = (Weapon) primaryItem;
				primWeapon.fire(attackDir, this);
			}
		}
	}

	public void setIdleFrame(int idleFrame) {
		getSprite()
				.setRegion(TextureManager.player.getRegions().get(idleFrame));
	}

	public void setAttackAnim(Animation a, float s) {
		attackAnim = a;
		animSpeed = s;
	}

	public void setAnimTime(float t) {
		animTime = t;
	}

	public void setMoveDir(Vector2 target) {
		moveDir = target;
	}

	public void setAttackDir(Vector2 target) {
		attackDir = target;
	}

	public int getAmmoCount(ammoType t) {
		if (t == ammoType.PISTOL) {
			return ammoCount[0];
		} else if (t == ammoType.SHOTGUN) {
			return ammoCount[1];
		} else if (t == ammoType.AR) {
			return ammoCount[2];
		} else if (t == ammoType.BOMB) {
			return ammoCount[3];
		} else {
			return -1;
		}
	}

	public void useAmmo(ammoType t, int count) {

		if (t == ammoType.PISTOL) {
			ammoCount[0] -= count;
		} else if (t == ammoType.SHOTGUN) {
			ammoCount[1] -= count;

		} else if (t == ammoType.AR) {
			ammoCount[2] -= count;
		} else if (t == ammoType.BOMB) {
			ammoCount[3] -= count;
		}
	}

	public void draw(Batch b, float parentAlpha) {
		super.draw(b, parentAlpha);
		if (hitIndicatorTimer > hitIndicatorTime) {
			getSprite().setColor(Color.WHITE);
		}
	}

	public float health = 100;
	float hitIndicatorTime = 0.5f;
	float hitIndicatorTimer = 0;

	public void Hurt(float damage) {
		getSprite().setColor(1f, 0.7f, 0.7f, 1f);
		health -= damage;
		hitIndicatorTimer = 0;
		hurtRumbleTimer = 0;
		SPGame.screenShake = true;
		rumbling = true;
		if (health < 0) {
			// Game over
			SPGame.screenShake = false;
			SPGame.game
					.setScreen(new GameOver(SPGame.game, SPGame.getPlayer()));

		}
	}

	public double getKills() {
		return kills;
	}

	public void setKills(double kills) {
		this.kills = kills;
	}

	public double getZombieKills() {
		return zombieKills;
	}

	public void setZombieKills(double zombieKills) {
		this.zombieKills = zombieKills;
	}

	public double getGhostKills() {
		return ghostKills;
	}

	public void setGhostKills(double ghostKills) {
		this.ghostKills = ghostKills;
	}

	public double getShotsFired() {
		return shotsFired;
	}

	public void setShotsFired(double shotsFired) {
		this.shotsFired = shotsFired;
	}

	public double getArtifactsFound() {
		return artifactsFound;
	}

	public void setArtifactsFound(double artifactsFound) {
		this.artifactsFound = artifactsFound;
	}

}
