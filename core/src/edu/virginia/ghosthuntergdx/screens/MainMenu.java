/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenu implements Screen {

	private Stage stage; // done
	private TextureAtlas atlas, backAtlas; // done
	private Skin skin; // done
	private Table table; // done
	private TextButton buttonPlay, buttonExit, buttonSettings; // done
	private BitmapFont redChiller, chillerFont; // done
	private Label heading;

	GhostHunterGame game;

	public MainMenu(GhostHunterGame ghostHunterGame) {
		this.game = ghostHunterGame;
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

		Texture back = new Texture("img/background.png");
		TextureRegion backg = new TextureRegion(back);
		TextureRegionDrawable background = new TextureRegionDrawable(backg);

		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/redButtons.pack");
		skin = new Skin(atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// creating fonts
		redChiller = new BitmapFont(Gdx.files.internal("Font/redChiller.fnt"),
				false);

		chillerFont = new BitmapFont(
				Gdx.files.internal("Font/chillerfont.fnt"), false);
		chillerFont.setScale(3);

		// creating buttons
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("redButton.Up");
		textButtonStyle.down = skin.getDrawable("redButton.Down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = chillerFont;

		buttonExit = new TextButton("EXIT", textButtonStyle);
		buttonExit.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				Gdx.app.exit();
			}

		});
		buttonExit.pad(20);

		buttonPlay = new TextButton("Play", textButtonStyle);

		buttonPlay.pad(20);

		buttonPlay.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new SPGame(game, 1, 1));
			}

		});
		buttonSettings = new TextButton("Settings", textButtonStyle);
		buttonSettings.pad(20);
		buttonSettings.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new MainSettingsMenu(game));
			}

		});

		// created heading
		LabelStyle headingStyle = new LabelStyle(redChiller, Color.WHITE);

		heading = new Label("Ghost Hunter", headingStyle);
		heading.setFontScale(9);

		// putting stuff together
		table.setBackground(background);
		table.add(heading).expandX().colspan(2).spaceBottom(100);
		table.row();
		table.add(buttonPlay).expandX().width(430).expandY().height(130);
		table.add(buttonSettings).expandX().width(430).expandY().height(130);
		table.row();
		table.add(buttonExit).center().expandX().colspan(2).width(430)
				.expandY().height(130).spaceTop(70);
		// table.debug();
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
