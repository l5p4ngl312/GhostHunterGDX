package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainSettingsMenu extends SettingsMenu implements Screen {

	private TextButton buttonPlay;
	private Stage stage = super.stage;
	private Table table = super.table;
	private TextButtonStyle textButtonStyle = super.textButtonStyle;
	

	public MainSettingsMenu(GhostHunterGame ghostHunterGame) {
		super(ghostHunterGame);
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

		buttonPlay = new TextButton("PLAY", textButtonStyle);
		buttonPlay.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new SPGame(game, 1, 1));

			}

		});
		buttonPlay.pad(20);
		
		table.row();
		table.add(buttonPlay).fill();
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
