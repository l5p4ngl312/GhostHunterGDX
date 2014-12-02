/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 */

package edu.virginia.ghosthuntergdx.screens;

import com.badlogic.gdx.Game;

import edu.virginia.ghosthuntergdx.assets.TextureManager;

public class GhostHunterGame extends Game {
	public static final String TITLE = "Ghost Hunter GDX";

	// Test edit
	@Override
	public void create() {
		setScreen(new Splash(this));
	}

}
