package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Game;

public class GhostHunterGame extends Game {
	public static final String TITLE = "Ghost Hunter GDX";
	
	//Test edit
	@Override
	public void create () {
		TextureManager.LoadTextures();
		setScreen(new Splash(this));
	}

}
