package edu.virginia.ghosthuntergdx.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;



public class Splash implements Screen {

	private Texture texture = new Texture(
			Gdx.files.internal("img/testlogo.jpg"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private GhostHunterGame game;
	private float time;
	private BitmapFont font;
	private CharSequence str1;
	private CharSequence str2;
	private Label header;
	private Label footer;

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
		font.setScale(1);
		
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		
		str1 = "CREATED BY ANTHONY BATRES, ALEXANDER MAZZA, DAVID RUBIN, LANE SPANGLER";
		header = new Label(str1, style);
		header.setX((Gdx.graphics.getWidth() / 2) - header.getWidth() / 2);
		header.setY(3 * (Gdx.graphics.getHeight()) / 4);
		stage.addActor(header);
		
		str2 = "UNIVERSITY OF VIRGINIA, CS 2110, FALL 2014";
		footer = new Label(str2, style);
		footer.setX((Gdx.graphics.getWidth() / 2) - footer.getWidth() / 2);
		footer.setY(2*(Gdx.graphics.getHeight() )/ 9);
		stage.addActor(footer);

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

	}

}