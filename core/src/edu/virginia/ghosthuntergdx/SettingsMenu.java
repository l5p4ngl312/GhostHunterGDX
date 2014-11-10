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

public class SettingsMenu implements Screen {

	protected Stage stage;
	protected TextureAtlas atlas;
	protected Skin skin;
	protected Table table;
	protected TextButton buttonDifficulty, buttonHome;
	protected BitmapFont font;
	protected Label heading;
	protected TextButtonStyle textButtonStyle;

	GhostHunterGame game;

	public SettingsMenu(GhostHunterGame ghostHunterGame) {
		this.game = ghostHunterGame;
	}
	
	public SettingsMenu(SPGame spGame){
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
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
		buttonDifficulty.pad(20);
		
		buttonHome = new TextButton("HOME", textButtonStyle);
		buttonHome.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new MainMenu(game));
			}

		});
		buttonHome.pad(20);
		

		
		// creates heading
		LabelStyle headingStyle = new LabelStyle(font, Color.WHITE);

		heading = new Label("SETTINGS", headingStyle);
		heading.setFontScale(2);

		// puts stuff together
		table.add(heading);
		table.row();
		table.add(buttonDifficulty).fill();
		table.row();
		table.add(buttonHome).fill();

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
