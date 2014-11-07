package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {

	public static Texture player;
	public static Texture mKnob;
	public static Texture mBack;
	public static Texture aKnob;
	public static Texture aBack;
	
	public static void LoadTextures()
	{
		player = new Texture(Gdx.files.internal("data/playerHead.png"));
		mKnob = new Texture(Gdx.files.internal("data/mKnob.png"));
		mBack = new Texture(Gdx.files.internal("data/mBack.png"));
		aKnob = new Texture(Gdx.files.internal("data/aKnob.png"));
		aBack = new Texture(Gdx.files.internal("data/aBack.png"));
	}
	
	public static void DisposeTextures()
	{
		player.dispose();
		mKnob.dispose();
		mBack.dispose();
		aKnob.dispose();
		aBack.dispose();
	}
}
