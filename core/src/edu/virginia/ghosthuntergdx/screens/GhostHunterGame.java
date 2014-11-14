package edu.virginia.ghosthuntergdx.screens;

import com.badlogic.gdx.Game;

import edu.virginia.ghosthuntergdx.assets.TextureManager;

public class GhostHunterGame extends Game {
	public static final String TITLE = "Ghost Hunter GDX";
	
	//Test edit
	@Override
	public void create () {
		TextureManager.LoadTextures();
		setScreen(new Splash(this));
	}

}
