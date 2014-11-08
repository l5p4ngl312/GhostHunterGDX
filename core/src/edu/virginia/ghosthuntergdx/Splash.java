package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Splash implements Screen {

	private Texture texture = new Texture(
			Gdx.files.internal("img/testlogo.jpg"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private GhostHunterGame game;
	private float time;

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
		if (time > 5){
			game.setScreen(new MainMenu(game));
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		// splashImage.setX(5); //I don't know what the parameter should be
		// splashImage.setY(5); //same as above
		stage.addActor(splashImage);

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