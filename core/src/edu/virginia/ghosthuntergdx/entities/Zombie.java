/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.Collider;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Bullet;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Zombie extends Enemy implements Collider {

	float speed = 1.8f;

	private float linearDamping = 5.0f;
	public static TextureRegion idle;
	private Animation attack;
	private Animation hit;
	private float attackSpeed = 2.5f;
	private float hitSpeed = 3.5f;

	private int[] attackFrames = { 3, 4, 2 };
	private int[] hitFrames = { 1, 2 };

	public Zombie(Vector2 position) {
		super(position, idle, idle.getRegionWidth(),
				(idle.getRegionHeight() / 2));
		getSprite().setSize(
				idle.getRegionWidth() / Consts.PIXEL_TO_METER
						* Consts.BOX_TO_WORLD,
				idle.getRegionHeight() / Consts.PIXEL_TO_METER
						* Consts.BOX_TO_WORLD);
		getSprite().setOrigin(getSprite().getWidth() / 2,
				getSprite().getHeight() - getSprite().getHeight() / 4);

		mBody.setLinearDamping(linearDamping);

		health = 10;
		Array<TextureRegion> animRegions = new Array<TextureRegion>();
		for (int i = 0; i < attackFrames.length; i++)
			animRegions.add(TextureManager.zombie.getRegions().get(
					attackFrames[i]));
		attack = new Animation(1 / attackSpeed, animRegions);

		animRegions = new Array<TextureRegion>();
		for (int i = 0; i < hitFrames.length; i++)
			animRegions.add(TextureManager.zombie.getRegions()
					.get(hitFrames[i]));
		hit = new Animation(1 / hitSpeed, animRegions);
	}

	public void checkDeath() {
		if (health <= 0) {
			this.remove();
			SPGame.getPickUpGroup().addActor(this);
			Array<AtlasRegion> regions = TextureManager.zombie.getRegions();
			TextureRegion deadR = (TextureRegion) regions.get(0);
			getSprite().setRegion(deadR);
			getSprite().setSize(
					deadR.getRegionWidth() / Consts.PIXEL_TO_METER
							* Consts.BOX_TO_WORLD,
					deadR.getRegionHeight() / Consts.PIXEL_TO_METER
							* Consts.BOX_TO_WORLD);
			getSprite().setOrigin(getSprite().getWidth() / 2, 0);

			double zKills = SPGame.getPlayer().getZombieKills();
			SPGame.getPlayer().setZombieKills(zKills + 1);

			double kills = SPGame.getPlayer().getKills();
			SPGame.getPlayer().setKills(kills + 1);

			SPGame.destroyBody(this.mBody);
			dead = true;
			// currentAnim = null;
		}
	}

	private final float attackCooldown = 1.4f;
	private float attackTimer = attackCooldown;
	boolean aggravated = false;
	boolean dead = false;

	@Override
	public void act(float delta) {
		if (!dead) {
			super.act(delta);
			attackTimer += delta;

			checkDeath();
			// moving the zombie towards the player
			Vector2 playerPos = SPGame.getPlayer().mBody.getPosition();
			Vector2 dir = playerPos.sub(mBody.getPosition());

			float dist = dir.len();
			if (dist < 4 || aggravated) {
				aggravated = true;
				dir.nor();
				moveDir = dir;
				lookAtTarget(delta);

				if (dist <= 1.1f) {
					attack();
				} else {
					mBody.setLinearVelocity(dir.scl(speed));
				}

				if (dist > 6.5f) {
					aggravated = false;
				}

			} else {
				moveTimer += delta;
				if (moveTimer > moveTime) {
					moveTime = (float) Math.random() * 6f;
					getNewMoveDir();
				}
				lookAtTarget(delta);
				dir = moveDir.nor();
				mBody.setLinearVelocity(dir.scl(speed));
			}

			checkAttackHit();
		}

	}

	float moveTime = (float) Math.random() * 6f;
	float moveTimer = 0;

	private void getNewMoveDir() {
		moveTimer = 0;
		if (!moveDir.equals(Vector2.Zero)) {
			moveDir = Vector2.Zero;
			moveTime = (float) Math.random() * 3f;
		} else {
			moveDir = new Vector2((float) Math.random() * 2 - 1,
					(float) Math.random() * 2 - 1);
		}
	}

	private void attack() {
		if (attackTimer > attackCooldown) {
			currentAnim = attack;
			animTime = 0;
			attackTimer = 0;
			SoundManager.zombieAttack.play(0.3f);
			didDamage = false;
		}
	}

	boolean didDamage = false;

	private void checkAttackHit() {
		if (currentAnim != null) {
			if (currentAnim.equals(attack)) {
				if (animTime > 0.5f) {
					if ((SPGame.getPlayer().mBody.getPosition().sub(mBody
							.getPosition())).len() <= 1.375f && !didDamage) {
						SPGame.getPlayer().Hurt(damage);
						didDamage = true;
					}
				}
			}
		}
	}

	@Override
	public void OnCollisionBegin(Body other, Contact c) {
		if (other.getUserData() != null) {
			if (other.getUserData() instanceof Bullet) {
				Bullet b = (Bullet) other.getUserData();
				this.health = health - b.damage;
				SoundManager.zombieHit.play(0.3f);
				aggravated = true;
				boolean attacking = false;
				if (currentAnim != null) {
					if (currentAnim.equals(attack)) {
						attacking = true;
						if (currentAnim.getKeyFrameIndex(animTime) == 2) {
							currentAnim = hit;
							animTime = 0;
						}
					}
				}
				if (!attacking) {
					currentAnim = hit;
					animTime = 0;
				}
			}
		}
	}

	@Override
	public void OnCollisionEnd(Body other, Contact c) {

	}
}
