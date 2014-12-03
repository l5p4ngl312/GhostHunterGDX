/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.entities.Ghost;
import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.entities.Zombie;

public class TextureManager {

	public static TextureAtlas player;
	public static Texture mKnob;
	public static Texture mBack;
	public static Texture aKnob;
	public static Texture aBack;
	public static TextureAtlas items;
	public static Texture bullet;
	public static Texture bulletImpact;
	public static Texture itemSlot;
	public static TextureAtlas ghost;
	public static TextureAtlas zombie;
	public static TextureRegion ghostR;
	public static TextureRegion zombieR;
	public static Texture artifact;
	public static Texture destroyedArtifact;

	public static final int playerIdleFrame = 2;

	public static void LoadTextures() {
		player = new TextureAtlas(Gdx.files.internal("data/playerAtlas.pack"));
		items = new TextureAtlas(Gdx.files.internal("data/itemAtlas.pack"));
		Array<AtlasRegion> regions = player.getRegions();
		Player.idleFists = (TextureRegion) regions.get(playerIdleFrame);
		mKnob = new Texture(Gdx.files.internal("data/mKnob.png"));
		mBack = new Texture(Gdx.files.internal("data/mBack.png"));
		aKnob = new Texture(Gdx.files.internal("data/aKnob.png"));
		aBack = new Texture(Gdx.files.internal("data/aBack.png"));
		bullet = new Texture(Gdx.files.internal("data/bullet.png"));
		bulletImpact = new Texture(Gdx.files.internal("data/bulletImpact.png"));
		itemSlot = new Texture(Gdx.files.internal("ui/itemSlot.png"));
		// vampire = new Texture(Gdx.files.internal("data/vampire1.png"));
		// warewolf = new Texture(Gdx.files.internal("data/warewolf.png"));
		zombie = new TextureAtlas(Gdx.files.internal("data/zombieAtlas.atlas"));
		ghost = new TextureAtlas(Gdx.files.internal("data/ghostAtlas.atlas"));
		regions = zombie.getRegions();
		Zombie.idle = (TextureRegion) regions.get(4);
		regions = ghost.getRegions();
		Ghost.idle = (TextureRegion) regions.get(2);

		artifact = new Texture(Gdx.files.internal("data/artifact1.png"));
		destroyedArtifact = new Texture(
				Gdx.files.internal("data/artifact1d.png"));

	}

	public static void DisposeTextures() {
		player.dispose();
		items.dispose();
		mKnob.dispose();
		mBack.dispose();
		aKnob.dispose();
		aBack.dispose();
		bullet.dispose();
		bulletImpact.dispose();
		// vampire.dispose();
		ghost.dispose();
		zombie.dispose();
		artifact.dispose();
		destroyedArtifact.dispose();
		// warewolf.dispose();
	}
}
