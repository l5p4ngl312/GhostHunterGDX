package edu.virginia.ghosthuntergdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import edu.virginia.ghosthuntergdx.entities.Player;

public class GameOver implements Screen {

	private Stage stage;
	private TextureAtlas atlas, backAtlas;
	private Skin skin;
	private Table table;
	private TextButton buttonMainMenu, buttonExit;
	private BitmapFont redChiller, chillerFont;
	private Label stats, killsH, zombieKillsH, ghostKillsH, shotsFiredH, artifactsFoundH, scoreH;
	public double kills, zombieKills, ghostKills, shotsFired, artifactsFound, score;

	GhostHunterGame game = new GhostHunterGame();

	public GameOver(GhostHunterGame ghg, Player p) {
		
		shotsFired = p.getShotsFired();
		zombieKills = p.getZombieKills();
		ghostKills = p.getGhostKills();
		kills = p.getKills();
		artifactsFound = p.getArtifactsFound();
		score = zombieKills + ghostKills*3 + artifactsFound*2;
		
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
		// TODO Auto-generated method stub

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
		
		chillerFont = new BitmapFont(Gdx.files.internal("Font/chillerfont.fnt"), false);

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

		buttonMainMenu = new TextButton("Main Menu", textButtonStyle);

		buttonMainMenu.pad(20);

		buttonMainMenu.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new MainMenu(game));
			}

		});

		// created headings for all the statistics 
		LabelStyle headingStyle = new LabelStyle(redChiller, Color.WHITE);

		stats = new Label("Statistics", headingStyle);
		stats.setFontScale(2);
		
		
		String aF = Double.toString(artifactsFound);
		artifactsFoundH = new Label("Artifacts Found : " + aF, headingStyle);
		artifactsFoundH.setFontScale(2);
		
		
		String gK = Double.toString(ghostKills);
		ghostKillsH = new Label("Ghosts Killed :   " +gK, headingStyle);
		ghostKillsH.setFontScale(2);
	
		
		String k = Double.toString(kills);
		killsH = new Label("Total Kills :     " + k, headingStyle);
		killsH.setFontScale(2);

		
		String sF = Double.toString(shotsFired);
		shotsFiredH = new Label("Shots Fired :     " +sF, headingStyle);
		shotsFiredH.setFontScale(2);
		
		
		String zK = Double.toString(zombieKills);
		zombieKillsH = new Label("Zombies Killed :  " + zK , headingStyle);
		zombieKillsH.setFontScale(2);
		
		String sc = Double.toString(score);
		scoreH = new Label("Total Score :     " + sc , headingStyle);
		scoreH.setFontScale(2);
		
		table.setBackground(background);
		table.add(stats).colspan(2);
		table.row();
		table.add(shotsFiredH);
		table.row();
		table.add(artifactsFoundH);
		table.row();
		table.add(ghostKillsH);
		table.row();
		table.add(zombieKillsH);
		table.row();
		table.add(killsH);
		table.row();
		table.add(scoreH).spaceBottom(100);;
		table.row();
		table.add(buttonMainMenu).expandX();
		table.add(buttonExit).expandX();
		table.debug();
		stage.addActor(table);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
