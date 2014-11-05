package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {

	public static Texture player;
	
	public static void LoadTextures()
	{
		player = new Texture(Gdx.files.internal("data/playerHead.png"));
	}
	
	public static void DisposeTextures()
	{
		player.dispose();
	}
}
