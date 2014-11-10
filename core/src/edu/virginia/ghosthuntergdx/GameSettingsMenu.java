package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class GameSettingsMenu extends SettingsMenu implements Screen {

	private TextButton buttonBackToGame;
	private Stage stage = super.stage;
	private Table table = super.table;
	private TextButtonStyle textButtonStyle = super.textButtonStyle;
	private int diff;
	private int prog;
	

	public GameSettingsMenu(GhostHunterGame ghostHunterGame) {
		super(ghostHunterGame);
	}
	
	public GameSettingsMenu(SPGame spGame){
		super(spGame);
		this.diff = spGame.difficultyLevel;
		this.prog = spGame.playerProgress;
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		super.show();

		buttonBackToGame = new TextButton("BACK TO GAME", textButtonStyle);
		buttonBackToGame.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new SPGame(game, diff, prog));

			}

		});
		buttonBackToGame.pad(20);
		
		table.row();
		table.add(buttonBackToGame).fill();
		table.debug();
		stage.addActor(table);
		
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
