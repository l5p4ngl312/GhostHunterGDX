/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.items.Item;
import edu.virginia.ghosthuntergdx.items.Weapon.ammoType;

public class GameUI extends Actor {

	Sprite primarySlot;
	Sprite secondarySlot;

	public Sprite ammoIcon;
	public static final float ammoIconScale = 1.3f;

	public int clipCount = 0;
	public int reserveCount = 0;

	BitmapFont ammoCountFont;

	public GameUI() {
		primarySlot = new Sprite(TextureManager.itemSlot);
		secondarySlot = new Sprite(TextureManager.itemSlot);

		primarySlot.setSize(primarySlot.getWidth() / Item.itemPixelToWorld
				* Consts.BOX_TO_WORLD, primarySlot.getHeight()
				/ Item.itemPixelToWorld * Consts.BOX_TO_WORLD);
		secondarySlot.setSize(primarySlot.getWidth(), primarySlot.getHeight());

		primarySlot.setOrigin(primarySlot.getWidth(), primarySlot.getHeight());
		secondarySlot.setOrigin(secondarySlot.getWidth(),
				secondarySlot.getHeight());

		primarySlot.setPosition(
				Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 15
						- primarySlot.getOriginX() + 5,
				Gdx.graphics.getHeight() - 20 - primarySlot.getOriginY() + 5);
		secondarySlot.setPosition(
				Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 30
						- secondarySlot.getOriginX() + 5,
				Gdx.graphics.getHeight() - secondarySlot.getHeight() - 20
						- secondarySlot.getOriginY() - 5);

		primarySlot.setScale(Item.primaryIconScale + 0.05f);
		secondarySlot.setScale(Item.secondaryIconScale + 0.05f);

		ammoCountFont = new BitmapFont();
		ammoCountFont.setColor(new Color(1, 1, 1, 1));
		ammoCountFont.setScale(2f);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		primarySlot.draw(batch, parentAlpha);
		secondarySlot.draw(batch, parentAlpha);
		if (displayAmmoIcon) {
			ammoIcon.draw(batch, parentAlpha);
			ammoCountFont.draw(batch, reserveCount + " - " + clipCount,
					Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 7f
							- ammoIcon.getOriginX() + 5,
					Gdx.graphics.getHeight() - 10 - ammoIcon.getOriginY());
		}
	}

	boolean displayAmmoIcon = false;

	public void setAmmoIcon(ammoType t) {
		if (t == ammoType.PISTOL) {
			ammoIcon = new Sprite(TextureManager.items.getRegions().get(5));
			displayAmmoIcon = true;
		} else if (t == ammoType.SHOTGUN) {
			ammoIcon = new Sprite(TextureManager.items.getRegions().get(7));
			displayAmmoIcon = true;
		} else if (t == ammoType.AR) {
			ammoIcon = new Sprite(TextureManager.items.getRegions().get(9));
			displayAmmoIcon = true;
		} else if (t == ammoType.BOMB) {
			ammoIcon = new Sprite(TextureManager.items.getRegions().get(11));
			displayAmmoIcon = true;
		} else {
			displayAmmoIcon = false;
		}

		if (displayAmmoIcon) {
			ammoIcon.setSize(ammoIcon.getWidth() / Item.itemPixelToWorld
					* Consts.BOX_TO_WORLD, ammoIcon.getHeight()
					/ Item.itemPixelToWorld * Consts.BOX_TO_WORLD);
			ammoIcon.setOrigin(ammoIcon.getWidth(), ammoIcon.getHeight());
			ammoIcon.setPosition(
					Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 7f
							- ammoIcon.getOriginX() + 5,
					Gdx.graphics.getHeight() - 20 - ammoIcon.getOriginY() + 5);
			ammoIcon.setScale(ammoIconScale);
		}
	}
}
