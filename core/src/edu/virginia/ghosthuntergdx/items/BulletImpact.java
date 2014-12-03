/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class BulletImpact extends Actor {

	Sprite impact;
	private static final float LIFETIME = 0.075f;
	private float timeToDeath = LIFETIME;

	public BulletImpact(Vector2 pos, float rot) {
		SPGame.getProjectileGroup().addActor(this);
		Texture t = TextureManager.bulletImpact;
		impact = new Sprite(t);
		setWidth(t.getWidth() / Consts.PIXEL_TO_METER);
		setHeight(t.getHeight() / Consts.PIXEL_TO_METER);
		setOrigin(getWidth() / 2, getHeight());
		setPosition(pos.x, pos.y);

		impact.setSize(getWidth() * Consts.BOX_TO_WORLD, getHeight()
				* Consts.BOX_TO_WORLD);
		impact.setOrigin(getWidth() * Consts.BOX_TO_WORLD / 2, getHeight()
				* Consts.BOX_TO_WORLD);
		impact.setPosition(pos.x * Consts.BOX_TO_WORLD, pos.y
				* Consts.BOX_TO_WORLD);
		impact.setRotation(rot + 90);
	}

	@Override
	public void draw(Batch b, float parentAlpha) {
		super.draw(b, parentAlpha);
		impact.setPosition(getX() * Consts.BOX_TO_WORLD - impact.getOriginX(),
				getY() * Consts.BOX_TO_WORLD - impact.getOriginY());
		impact.draw(b, parentAlpha);
	}

	@Override
	public void act(float dt) {
		super.act(dt);
		timeToDeath -= dt;
		if (timeToDeath <= 0)
			this.remove();
	}
}
