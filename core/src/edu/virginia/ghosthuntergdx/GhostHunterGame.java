package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Game;

public class GhostHunterGame extends Game {
	public static final String TITLE = "Ghost Hunter GDX";
	
	@Override
	public void create () {
		setScreen(new Splash());
	}

}
