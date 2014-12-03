/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	public static Sound ghostAlert;
	public static Sound pistolShot;
	public static Sound footstep;
	public static Sound drop;
	public static Sound pickup;
	public static Sound reload;
	public static Sound dryFire;
	public static Sound zombieAttack;
	public static Sound zombieHit;

	public static void LoadSounds() {
		pistolShot = Gdx.audio.newSound(Gdx.files
				.internal("sound/pistolShot.mp3"));
		footstep = Gdx.audio.newSound(Gdx.files.internal("sound/footstep.wav"));
		drop = Gdx.audio.newSound(Gdx.files.internal("sound/drop.mp3"));
		pickup = Gdx.audio.newSound(Gdx.files.internal("sound/pickup.mp3"));
		ghostAlert = Gdx.audio.newSound(Gdx.files
				.internal("sound/ghostAlert.mp3"));
		reload = Gdx.audio.newSound(Gdx.files.internal("sound/reload.wav"));
		dryFire = Gdx.audio.newSound(Gdx.files.internal("sound/dryFire.wav"));
		zombieHit = Gdx.audio.newSound(Gdx.files
				.internal("sound/zombieHit.wav"));
		zombieAttack = Gdx.audio.newSound(Gdx.files
				.internal("sound/zombieMoan.wav"));
	}

	public static void DisposeSounds() {
		pistolShot.dispose();
		footstep.dispose();
		pickup.dispose();
		drop.dispose();
		reload.dispose();
		dryFire.dispose();
		zombieAttack.dispose();
		zombieHit.dispose();
		ghostAlert.dispose();
	}
}
