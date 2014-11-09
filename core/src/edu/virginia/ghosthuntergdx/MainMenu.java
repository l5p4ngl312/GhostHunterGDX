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

public class MainMenu implements Screen{

	private Stage stage; //done
	private TextureAtlas atlas; //done
	private Skin skin; //done
	private Table table; //done
	private TextButton buttonPlay, buttonExit, buttonSettings; //done
	private BitmapFont white, black; //done
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
		
		Gdx.input.setInputProcessor(stage);
		
		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);
		
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		//creating fonts
		white = new BitmapFont(Gdx.files.internal("Font/white.fnt"), false);
		black = new BitmapFont(Gdx.files.internal("Font/black.fnt"), false);
		
		
		//creating buttons
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = black;
		
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
				Gdx.app.exit();
			}
			
		});
		
		//created heading
		LabelStyle headingStyle = new LabelStyle(white, Color.WHITE);
		
		heading = new Label("Welcome To Ghost Hunter", headingStyle);
		heading.setFontScale(3);
		
		//putting stuff together
		table.add(heading);
		table.row();
		
		table.add(buttonPlay).fill();
		table.row();
		table.add(buttonExit).fill();
		table.row();
		table.add(buttonSettings).fill();
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
