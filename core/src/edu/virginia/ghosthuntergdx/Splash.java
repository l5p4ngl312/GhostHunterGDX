package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Splash implements Screen {

	private Texture texture = new Texture(
			Gdx.files.internal("img/testlogo.jpg"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private GhostHunterGame game;
	private float time;
	private SpriteBatch names;
	private BitmapFont font;
	private CharSequence str1;
	private SpriteBatch course;
	private CharSequence str2;

	public Splash(GhostHunterGame g) {
		this.game = g;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		time += delta;
		if (time > 5) {
			game.setScreen(new MainMenu(game));
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		// shows logo
		splashImage.setX((Gdx.graphics.getWidth() / 2)
				- (splashImage.getWidth()));
		splashImage.setY((Gdx.graphics.getHeight() / 2)
				- (splashImage.getHeight()));
		splashImage.setScale(2);
		stage.addActor(splashImage);

		font = new BitmapFont(Gdx.files.internal("Font/whiteimpact2.fnt"),
				Gdx.files.internal("Font/whiteimpact2_0.png"), false);
		font.setColor(255, 255, 255, 1);
		font.setScale(3);

		// shows names
		str1 = "CREATED BY ANOTHONY BATTRES, ALEXANDER MAZZA, DAVID RUBIN, LANE SPANGLER";
		names = new SpriteBatch();
		names.begin();
		font.draw(names, str1, Gdx.graphics.getWidth() / 2,
				3 * (Gdx.graphics.getHeight()) / 4);
		names.end();

		// shows class info
		str2 = "UNIVERSITY OF VIRGINIA, CS 2110, FALL 2014";
		course = new SpriteBatch();
		course.begin();
		font.draw(course, str2, Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 4);
		course.end();

	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		texture.dispose();
		stage.dispose();
		names.dispose();

	}

}