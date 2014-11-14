package edu.virginia.ghosthuntergdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	public static Sound pistolShot;
	public static Sound footstep;
	public static Sound drop;
	public static Sound pickup;
	
	public static void LoadSounds()
	{
		pistolShot = Gdx.audio.newSound(Gdx.files.internal("sound/pistolShot.mp3"));
		footstep = Gdx.audio.newSound(Gdx.files.internal("sound/footstep.wav"));
		drop = Gdx.audio.newSound(Gdx.files.internal("sound/drop.mp3"));
		pickup = Gdx.audio.newSound(Gdx.files.internal("sound/pickup.mp3"));
	}
	
	public static void DisposeSounds()
	{
		pistolShot.dispose();
		footstep.dispose();
		pickup.dispose();
		drop.dispose();
	}
}
