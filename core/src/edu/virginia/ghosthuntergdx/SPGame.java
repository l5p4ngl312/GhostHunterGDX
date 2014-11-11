package edu.virginia.ghosthuntergdx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import edu.virginia.ghosthuntergdx.entities.*;

public class SPGame implements Screen {

	GhostHunterGame game;
	static World world;

	Vector2 playerStartPos = new Vector2(10, 5);
	OrthographicCamera camera;
	OrthographicCamera HUDcamera;

	Player player;
	Stage level;
	Stage HUDstage;

	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;

	Box2DDebugRenderer debugger;

	int difficultyLevel = 1;
	int playerProgress = 1;
	boolean debugPhysics = true;

	// for settings button:
	TextButton buttonOptions;
	TextButtonStyle textButtonStyle;
	BitmapFont font;
	Skin skin;
	TextureAtlas atlas;

	RayHandler rayHandler;
	public static final int raysPerLight = 128;
	public static final float lightDistance = 16f;
	
	PointLight playerLight;
	
	public SPGame(GhostHunterGame game, int difficultyLevel, int playerProgress) {
		this.game = game;
		this.difficultyLevel = difficultyLevel;
		this.playerProgress = playerProgress;
	}

	@Override
	public void render(float delta) {
		// Step through the Box2D physics simulation
		world.step(1 / 45f, 6, 2);

		// Clear the screen
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, delta);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		// Center the main camera on the player's sprite and update it
		camera.position.set(player.getSprite().getX(), player.getSprite()
				.getY(), 0);
		level.getCamera().update();

		// Expand the view matrix for the map renderer so that nothing pops in
		// visibly
		float x = camera.position.x - camera.viewportWidth * camera.zoom;
		float y = camera.position.y - camera.viewportHeight * camera.zoom;

		float width = camera.viewportWidth * camera.zoom * 2;
		float height = camera.viewportHeight * camera.zoom * 2;

		// Render the map
		mapRenderer.setView(camera.combined, x, y, width, height);
		mapRenderer.render();

		// Set the player's move and attack direction based on input from the
		// virtual joysticks
		player.setMoveDir(new Vector2(mPad.getKnobPercentX(), mPad
				.getKnobPercentY()));
		player.setAttackDir(new Vector2(aPad.getKnobPercentX(), aPad
				.getKnobPercentY()));

		// Update all the objects in the level
		level.act(delta);

		// Draw the level
		level.draw();

		rayHandler.setCombinedMatrix(camera.combined.scale(Consts.BOX_TO_WORLD,Consts.BOX_TO_WORLD,0));
		rayHandler.updateAndRender();
		// Draw the Box2D debug information if the flag is true
		if (debugPhysics)
			debugger.render(world, camera.combined.scale(Consts.BOX_TO_WORLD,
					Consts.BOX_TO_WORLD, 0));

		// Update the HUD camera, HUD elements, and draw the HUD
		HUDstage.getCamera().update();
		HUDstage.act(delta);
		HUDstage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// Update all viewports when the screen is resized
		level.getViewport().update(width, height, true);
		HUDstage.getViewport().update(width, height, true);
	}

	Touchpad mPad;

	Touchpad aPad;

	int currentMap = 0;

	@Override
	public void show() {
		// Set logcat to display gdx debug messages
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Create a box2D physics world with no gravity (it is a top down game)
		world = new World(new Vector2(0, 0), true);

		// Create a new camera with a viewport spanning the pixel dimensions of
		// the device's screen
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		// Make Y = 0 the bottom of the screen
		camera.setToOrtho(false);
		camera.update();

		// Make a similar camera that only displays HUD elements
		HUDcamera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		HUDcamera.setToOrtho(false);
		HUDcamera.update();

		// Load the current map's tmx file and create a renderer for it
		map = new TmxMapLoader().load("data/map" + currentMap + ".tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		// Build box2D collision bodies based on the map's Collision object
		// layer
		Physics.buildShapes(map, world, "Collision");

		// Set the player's default start position
		Vector2 startPos = playerStartPos;

		// Try to find an object layer called PlayerSpawn
		MapLayer spawnLayer = map.getLayers().get("PlayerSpawn");
		if (spawnLayer != null) {
			for (MapObject o : spawnLayer.getObjects()) {
				// If there is an ellipse object in this object layer, spawn the
				// player at its center
				if (o instanceof EllipseMapObject) {
					EllipseMapObject c = (EllipseMapObject) o;
					startPos = new Vector2(c.getEllipse().x
							/ Consts.BOX_TO_WORLD, c.getEllipse().y
							/ Consts.BOX_TO_WORLD);
					break;
				}
			}
		}

		// Create a new player object a the spawn position
		player = new Player(startPos);

		// Create the movement stick on the right side of the screen
		Skin mSkin = new Skin();
		mSkin.add("mBack", TextureManager.mBack);
		mSkin.add("mKnob", TextureManager.mKnob);

		TouchpadStyle mStyle = new TouchpadStyle();
		mStyle.knob = mSkin.getDrawable("mKnob");
		mStyle.background = mSkin.getDrawable("mBack");

		mPad = new Touchpad(10, mStyle);
		mPad.setBounds(Gdx.graphics.getWidth()
				- (250 + Gdx.graphics.getWidth() / 15),
				Gdx.graphics.getHeight() / 15, 250, 250);

		// Create the attack stick on the right side of the screen
		Skin aSkin = new Skin();
		aSkin.add("aBack", TextureManager.aBack);
		aSkin.add("aKnob", TextureManager.aKnob);

		TouchpadStyle aStyle = new TouchpadStyle();
		aStyle.knob = aSkin.getDrawable("aKnob");
		aStyle.background = aSkin.getDrawable("aBack");

		aPad = new Touchpad(10, aStyle);
		aPad.setBounds(Gdx.graphics.getWidth() / 15,
				Gdx.graphics.getHeight() / 15, 175, 175);

		// Create a group that all moving entities will be put in
		Group entities = new Group();

		// Create the stage for objects actually in the level
		level = new Stage();
		level.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), camera));

		// Add the entity group to this stage
		level.addActor(entities);

		// Add the player to the entity group
		entities.addActor(player);

		LevelDirector director = new LevelDirector(difficultyLevel,
				playerProgress, map);
		level.addActor(director);

		// Create a stage for hud elements
		HUDstage = new Stage();
		// Set libGDX to check for input from the virtual sticks
		Gdx.input.setInputProcessor(HUDstage);

		// Add the virtual sticks to the HUDstage and set its viewport
		HUDstage.addActor(mPad);
		HUDstage.addActor(aPad);
		HUDstage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), HUDcamera));

		// Create a box2D debug renderer for physics debugging
		debugger = new Box2DDebugRenderer(true, true, true, true, true, true);

		//Lighting
		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(new Color(0.1f,0.1f,0.1f,0.5f));
		rayHandler.setBlurNum(3);
		
		
		playerLight = new PointLight(rayHandler, raysPerLight, new Color(1,1,1,0.5f), 7, 5, 10);
		playerLight.setSoft(true);
		playerLight.setSoftnessLength(0.5f);
		Light.setContactFilter(Physics.LIGHT,Physics.LIGHT_GROUP,Physics.MASK_LIGHTS);
		playerLight.attachToBody(player.getBody(), 0, 0);
		
		// show settings button

		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);

		font = new BitmapFont(Gdx.files.internal("Font/whiteimpact2.fnt"),
				Gdx.files.internal("Font/whiteimpact2_0.png"), false);
		font.setColor(255, 255, 255, 1);
		font.setScale(2);

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = font;

		buttonOptions = new TextButton("OPTIONS", textButtonStyle);
		buttonOptions.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new GameSettingsMenu(new SPGame(game,
						difficultyLevel, playerProgress)));
			}
		});
		buttonOptions.pad(20);

		buttonOptions.setX(Gdx.graphics.getWidth() - buttonOptions.getWidth() - 30);
		buttonOptions.setY(Gdx.graphics.getHeight() - buttonOptions.getHeight() - 30);
		HUDstage.addActor(buttonOptions);

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
		world.dispose();
		map.dispose();
		level.dispose();
		HUDstage.dispose();
		debugger.dispose();
		mapRenderer.dispose();

	}

	public static World getPhysicsWorld() {
		// TODO Auto-generated method stub
		return world;
	}

}
