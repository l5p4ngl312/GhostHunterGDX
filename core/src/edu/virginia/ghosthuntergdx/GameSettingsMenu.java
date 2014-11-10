package edu.virginia.ghosthuntergdx;

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

public class GameSettingsMenu implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonDifficulty, buttonHome, buttonResume;
	private BitmapFont font;
	private Label heading;
	private TextButtonStyle textButtonStyle;
	private int diff;
	private int prog;
	private GhostHunterGame game;
	
	public GameSettingsMenu(SPGame spGame){
		this.diff = spGame.difficultyLevel;
		this.prog = spGame.playerProgress;
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

		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// creating font
		font = new BitmapFont(Gdx.files.internal("Font/whiteimpact2.fnt"),
				Gdx.files.internal("Font/whiteimpact2_0.png"), false);
		font.setColor(255, 255, 255, 1);
		font.setScale(2);

		// creating buttons
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");
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

		buttonResume = new TextButton("RESUME", textButtonStyle);
		buttonResume.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new SPGame(game, diff, prog));

			}

		});
		buttonResume.pad(40);

		// creates heading
		LabelStyle headingStyle = new LabelStyle(font, Color.WHITE);

		heading = new Label("OPTIONS", headingStyle);
		heading.setFontScale(4);

		// puts stuff together
		table.add(heading);
		table.row();
		table.add(buttonDifficulty).fill();
		table.row();
		table.add(buttonHome).fill();
		table.row();
		table.add(buttonResume).fill();
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