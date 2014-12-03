/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Enemy extends PhysicsActor {

	Vector2 position;
	Rectangle bounds;
	double health = 100;
	float speed;
	float damage = 10;
	float targetRot;
	Vector2 moveDir = new Vector2(0, 0);
	public float angleOffset = 90;
	public float rotSpeed = 900f;

	public Animation currentAnim;
	public float animTime = 1000f;

	public Enemy(Vector2 position, TextureRegion t, float bodyWidth,
			float bodyHeight) {
		super(position, new TextureRegion(t), Physics.ENEMYBODY,
				Physics.NO_GROUP, Physics.MASK_ENEMYBODY, bodyWidth,
				bodyHeight, false);
		this.position = position;

	}

	protected void lookAtTarget(float delta) {
		// Set the enemies target rotation based on either his move direction
		targetRot = getSprite().getRotation();
		if (moveDir.len() > 0) {
			targetRot = (float) Math.atan2(moveDir.y, moveDir.x)
					* MathUtils.radiansToDegrees + angleOffset;

		}
		// Rotate the enemy towards his target rotation along the shortest path
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
	}

	public void act(float delta) {
		super.act(delta);

		if (currentAnim != null) {
			getSprite().setRegion(currentAnim.getKeyFrame(animTime, false));
			animTime += delta;
		}

	}

}
