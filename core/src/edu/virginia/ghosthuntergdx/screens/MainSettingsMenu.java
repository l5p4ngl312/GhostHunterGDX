/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 */

package edu.virginia.ghosthuntergdx.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainSettingsMenu implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonDifficulty, buttonHome, buttonPlay;
	private BitmapFont font, headingFont;
	private Label heading;
	private TextButtonStyle textButtonStyle;

	GhostHunterGame game;

	public MainSettingsMenu(GhostHunterGame ghostHunterGame) {
		this.game = ghostHunterGame;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/redButtons.pack");
		skin = new Skin(atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// creating font
		font = new BitmapFont(Gdx.files.internal("Font/chillerfont.fnt"),
				Gdx.files.internal("Font/chillerfont_0.png"), false);
		font.setColor(255, 255, 255, 1);
		font.setScale(2);
		headingFont = new BitmapFont(Gdx.files.internal("Font/redChiller.fnt"),
				Gdx.files.internal("Font/redChiller_0.png"), false);
		headingFont.setColor(255, 255, 255, 1);

		// creating buttons
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("redButton.Up");
		textButtonStyle.down = skin.getDrawable("redButton.Down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = font;

		buttonDifficulty = new TextButton("DIFFICULTY", textButtonStyle);
		buttonDifficulty.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new DifficultyScreen(game));
			}

		});
		buttonDifficulty.pad(40);

		buttonHome = new TextButton("HOME", textButtonStyle);
		buttonHome.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new MainMenu(game));
			}

		});
		buttonHome.pad(40);

		buttonPlay = new TextButton("PLAY", textButtonStyle);
		buttonPlay.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new SPGame(game, 1, 1));

			}

		});
		buttonPlay.pad(40);

		// creates heading
		LabelStyle headingStyle = new LabelStyle(headingFont, Color.WHITE);

		heading = new Label("SETTINGS", headingStyle);
		heading.setFontScale(4);

		// puts stuff together
		table.add(heading);
		table.row();
		table.add(buttonDifficulty).fill();
		table.row();
		table.add(buttonHome).fill();
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