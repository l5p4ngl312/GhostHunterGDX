package edu.virginia.ghosthuntergdx.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import edu.virginia.ghosthuntergdx.CollisionListener;
import edu.virginia.ghosthuntergdx.GameInputListener;
import edu.virginia.ghosthuntergdx.GameUI;
import edu.virginia.ghosthuntergdx.LevelDirector;
import edu.virginia.ghosthuntergdx.Physics;
import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.assets.SoundManager;
import edu.virginia.ghosthuntergdx.assets.TextureManager;
import edu.virginia.ghosthuntergdx.entities.*;
import edu.virginia.ghosthuntergdx.items.Ammo;
import edu.virginia.ghosthuntergdx.items.Flashlight;
import edu.virginia.ghosthuntergdx.items.Pistol;
import edu.virginia.ghosthuntergdx.items.Weapon.ammoType;

public class SPGame implements Screen {

	public static GhostHunterGame game;
	static World world;

	Vector2 playerStartPos = new Vector2(10, 5);
	// ghost and zombie testing start position
	Vector2 zombieStartPos = new Vector2(2,2);
	Vector2 ghostStartPos = new Vector2(17,8);
	OrthographicCamera camera;
	static OrthographicCamera HUDcamera;

	static Player player;
	static Stage level;
	static Stage HUDstage;

	static Group pickUpGroup;
	static Group entityGroup;
	static Group projectileGroup;

	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;

	GameInputListener input;

	Box2DDebugRenderer debugger;

	int difficultyLevel = 1;
	int playerProgress = 1;

	// for game settings menu
	int gamestate = 0; // gamestate: 0=gameplay, 1=options, 2=difficulty,
						// 3=difficultyeasy, 4=difficultymedium,
						// 5=difficultyhard
	Stage settingsStage;
	Table settingsTable;
	TextureAtlas settingsAtlas;
	Skin settingsSkin;
	Label heading;

	// difficulty menu
	Stage difficultyStage, difficultyEasyStage, difficultyMediumStage,
			difficultyHardStage;
	Table difficultyTable;
	TextureAtlas difficultyAtlas;
	Skin difficultySkin;
	Label difficultyHeading;
	TextButton buttonEasy, buttonMedium, buttonHard,
			difficultyEasyButtonResume, difficultyMediumButtonResume,
			difficultyHardButtonResume;

	CharSequence easy, medium, hard;
	Label easyMessage, mediumMessage, hardMessage;

	Texture diffBack = new Texture("img/background.png");
	TextureRegion diffBackg = new TextureRegion(diffBack);
	TextureRegionDrawable diffBackground = new TextureRegionDrawable(diffBackg);
	
	SpriteBatch batch = new SpriteBatch();
	
	public static boolean debugPhysics = false;

	// for settings button:
	TextButton buttonOptions, settingsButtonDifficulty, settingsButtonHome,
			settingsButtonResume;
	TextButtonStyle textButtonStyle;
	BitmapFont font, headingFont, messageFont;
	Skin skin;
	TextureAtlas atlas;

	static RayHandler rayHandler;
	public static final int raysPerLight = 128;
	public static final float lightDistance = 16f;

	public SPGame(GhostHunterGame game, int difficultyLevel, int playerProgress) {
		this.game = game;
		this.difficultyLevel = difficultyLevel;
		this.playerProgress = playerProgress;
	}

	public static final float camLerp = 0.2f;
	public static final float camForwardOffset = 115f;

	public static boolean screenShake = false;
	public static final float screenShakeMaxMagnitude = 65f;

	private static Body groundBody;

	private static GameUI ui;
	
	private static LevelDirector director;

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, delta);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		if (gamestate == 0) {
			// Step through the Box2D physics simulation
			world.step(1 / 45f, 6, 2);
			activateBodies();
			deactivateBodies();
			deactivateLights();
			activateLights();
			destroyBodies();

			// Center the main camera on the player's sprite and update it
			Vector2 offsetVector = player.getForwardVector().scl(
					camForwardOffset);
			if (screenShake) {
				offsetVector.set(offsetVector.x + (float) Math.random() * 2
						* screenShakeMaxMagnitude - screenShakeMaxMagnitude,
						offsetVector.y + (float) Math.random() * 2
								* screenShakeMaxMagnitude
								- screenShakeMaxMagnitude);
			}

			camera.position.lerp(new Vector3(player.getSprite().getX()
					+ offsetVector.x, player.getSprite().getY()
					+ offsetVector.y, camera.position.z), camLerp);
			level.getCamera().update();

			// Expand the view matrix for the map renderer so that nothing pops
			// in
			// visibly
			float x = camera.position.x - camera.viewportWidth * camera.zoom;
			float y = camera.position.y - camera.viewportHeight * camera.zoom;

			float width = camera.viewportWidth * camera.zoom * 2;
			float height = camera.viewportHeight * camera.zoom * 2;

			// Render the map
			mapRenderer.setView(camera.combined, x, y, width, height);
			mapRenderer.render();

			// Set the player's move and attack direction based on input from
			// the
			// virtual joysticks
			player.setMoveDir(new Vector2(mPad.getKnobPercentX(), mPad
					.getKnobPercentY()));
			player.setAttackDir(new Vector2(aPad.getKnobPercentX(), aPad
					.getKnobPercentY()));

			// Update all the objects in the level
			level.act(delta);
			// Draw the level
			level.draw();

			if (!debugPhysics) {
				rayHandler.setCombinedMatrix(camera.combined.scale(
						Consts.BOX_TO_WORLD, Consts.BOX_TO_WORLD, 0));
				rayHandler.updateAndRender();
			}
			// Draw the Box2D debug information if the flag is true
			if (debugPhysics)
				debugger.render(world, camera.combined.scale(
						Consts.BOX_TO_WORLD, Consts.BOX_TO_WORLD, 0));

			// Update the HUD camera, HUD elements, and draw the HUD
			HUDstage.getCamera().update();
			HUDstage.act(delta);
			HUDstage.draw();

		} else if (gamestate == 1) {
			settingsStage.act(delta);
			settingsStage.draw();
		} else if (gamestate == 2) {
			difficultyStage.act(delta);
			difficultyStage.draw();
		} else if (gamestate == 3) {
			batch.begin();
			batch.draw(diffBack, 0, 0);
			batch.end();
//			this.mediumHardDispose();
			difficultyEasyStage.act(delta);
			difficultyEasyStage.draw();
		} else if (gamestate == 4) {
			batch.begin();
			batch.draw(diffBack, 0, 0);
			batch.end();
//			this.easyHardDispose();
			difficultyMediumStage.act(delta);
			difficultyMediumStage.draw();
		} else if (gamestate == 5) {
			batch.begin();
			batch.draw(diffBack, 0, 0);
			batch.end();
//			this.easyMediumDispose();
			difficultyHardStage.act(delta);
			difficultyHardStage.draw();
		}
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

		TextureManager.LoadTextures();
		SoundManager.LoadSounds();
		// Create a box2D physics world with no gravity (it is a top down game)
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new CollisionListener());
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

		MapLayer groundLayer = map.getLayers().get("Ground");
		for (MapObject o : groundLayer.getObjects()) {
			if (o instanceof RectangleMapObject) {
				RectangleMapObject r = (RectangleMapObject) o;
				Shape shape = Physics.getRectangle(r);
				BodyDef bd = new BodyDef();
				bd.type = BodyType.StaticBody;
				Body body = world.createBody(bd);
				FixtureDef fDef = new FixtureDef();
				fDef.shape = shape;
				fDef.density = 1;
				fDef.filter.categoryBits = Physics.GROUND;
				fDef.filter.groupIndex = Physics.NO_GROUP;
				fDef.filter.maskBits = Physics.NO_GROUP;
				fDef.isSensor = true;
				body.createFixture(fDef);
				groundBody = body;
			}
		}

		// Lighting
		if (!debugPhysics) {
			RayHandler.setGammaCorrection(true);
			RayHandler.useDiffuseLight(false);

			rayHandler = new RayHandler(world);
			rayHandler.setAmbientLight(new Color(0f, 0f, 0f, 0.4f));
			rayHandler.setBlurNum(1);

			Light.setContactFilter(Physics.LIGHT, Physics.LIGHT_GROUP,
					Physics.MASK_LIGHTS);
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
		entityGroup = new Group();
		// Create item pickup group
		pickUpGroup = new Group();
		// Create projectile group
		projectileGroup = new Group();

		// Create the stage for objects actually in the level
		level = new Stage();
		level.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), camera));

		// Add the entity group to this stage
		level.addActor(pickUpGroup);
		level.addActor(projectileGroup);
		level.addActor(entityGroup);

		// Add the player to the entity group
		entityGroup.addActor(player);


		// Create a stage for hud elements
		HUDstage = new Stage();
		// Set libGDX to check for input from the virtual sticks and game
		// listener
		final InputMultiplexer multiInput = new InputMultiplexer();
		input = new GameInputListener();
		multiInput.addProcessor(HUDstage);
		multiInput.addProcessor(input);
		Gdx.input.setInputProcessor(multiInput);

		// Add the virtual sticks to the HUDstage and set its viewport
		HUDstage.addActor(mPad);
		HUDstage.addActor(aPad);
		HUDstage.addActor(input);
		HUDstage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), HUDcamera));
		
		director = new LevelDirector(difficultyLevel,map);
		level.addActor(director);
		// Create a box2D debug renderer for physics debugging
		debugger = new Box2DDebugRenderer(true, true, true, true, true, true);

		// show settings button

		atlas = new TextureAtlas("ui/redButtons.pack");
		skin = new Skin(atlas);

		font = new BitmapFont(Gdx.files.internal("Font/chillerfont.fnt"),
				Gdx.files.internal("Font/chillerfont_0.png"), false);
		font.setColor(255, 255, 255, 1);
		font.setScale(2);

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("redButton.Up");
		textButtonStyle.down = skin.getDrawable("redButton.Down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = font;

		buttonOptions = new TextButton("OPTIONS", textButtonStyle);
		buttonOptions.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				gamestate = 1;
				Gdx.input.setInputProcessor(settingsStage);

			}
		});
		buttonOptions.pad(20);

		buttonOptions.setX(30);
		buttonOptions
				.setY(Gdx.graphics.getHeight() - buttonOptions.getHeight());
		HUDstage.addActor(buttonOptions);

		ui = new GameUI();
		HUDstage.addActor(ui);

		settingsStage = new Stage();

		settingsAtlas = new TextureAtlas("ui/redButtons.pack");
		settingsSkin = new Skin(settingsAtlas);

		settingsTable = new Table(skin);
		settingsTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		settingsButtonDifficulty = new TextButton("DIFFICULTY", textButtonStyle);
		settingsButtonDifficulty.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				gamestate = 2;
				Gdx.input.setInputProcessor(difficultyStage);

			}

		});
		settingsButtonDifficulty.pad(40);

		settingsButtonHome = new TextButton("HOME", textButtonStyle);
		settingsButtonHome.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				game.setScreen(new MainMenu(game));

			}

		});
		settingsButtonHome.pad(40);

		settingsButtonResume = new TextButton("RESUME", textButtonStyle);
		settingsButtonResume.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				gamestate = 0;
				Gdx.input.setInputProcessor(multiInput);

			}

		});
		settingsButtonResume.pad(40);

		// creates heading
		headingFont = new BitmapFont(Gdx.files.internal("Font/redChiller.fnt"),
				Gdx.files.internal("Font/redChiller_0.png"), false);
		headingFont.setColor(255, 255, 255, 1);

		LabelStyle headingStyle = new LabelStyle(headingFont, Color.WHITE);

		heading = new Label("OPTIONS", headingStyle);
		heading.setFontScale(4);

		// puts stuff together
		settingsTable.add(heading);
		settingsTable.row();
		settingsTable.add(settingsButtonDifficulty).fill();
		settingsTable.row();
		settingsTable.add(settingsButtonHome).fill();
		settingsTable.row();
		settingsTable.add(settingsButtonResume).fill();
		settingsTable.debug();
		settingsStage.addActor(settingsTable);

		difficultyStage = new Stage();
		difficultyEasyStage = new Stage();
		difficultyMediumStage = new Stage();
		difficultyHardStage = new Stage();

		difficultyAtlas = new TextureAtlas("ui/redButtons.pack");
		difficultySkin = new Skin(difficultyAtlas);

		difficultyTable = new Table(difficultySkin);
		difficultyTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		difficultyTable.setBackground(diffBackground);
		
		buttonEasy = new TextButton("EASY", textButtonStyle);
		buttonEasy.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				gamestate = 3;
				Gdx.input.setInputProcessor(difficultyEasyStage);

			}

		});
		buttonEasy.pad(40);

		buttonMedium = new TextButton("MEDIUM", textButtonStyle);
		buttonMedium.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				gamestate = 4;
				Gdx.input.setInputProcessor(difficultyMediumStage);

			}
		});
		buttonMedium.pad(40);

		buttonHard = new TextButton("HARD", textButtonStyle);
		buttonHard.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				gamestate = 5;
				Gdx.input.setInputProcessor(difficultyHardStage);

			}
		});
		buttonHard.pad(40);

		difficultyHeading = new Label("DIFFICULTY", headingStyle);
		difficultyHeading.setFontScale(4);

		// puts stuff together
		difficultyTable.add(difficultyHeading);
		difficultyTable.row();
		difficultyTable.add(buttonEasy).fill();
		difficultyTable.row();
		difficultyTable.add(buttonMedium).fill();
		difficultyTable.row();
		difficultyTable.add(buttonHard).fill();
		difficultyTable.debug();
		difficultyStage.addActor(difficultyTable);

		messageFont = new BitmapFont(
				Gdx.files.internal("Font/redChiller.fnt"),
				Gdx.files.internal("Font/redChiller_0.png"), false);
		messageFont.setScale(4);

		LabelStyle messageStyle = new LabelStyle(messageFont, Color.RED);

		difficultyEasyButtonResume = new TextButton("RESUME", textButtonStyle);
		difficultyEasyButtonResume.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				difficultyLevel = 1;
				gamestate = 0;
				Gdx.input.setInputProcessor(multiInput);

			}
		});
		difficultyEasyButtonResume.pad(80);

		difficultyMediumButtonResume = new TextButton("RESUME", textButtonStyle);
		difficultyMediumButtonResume.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				difficultyLevel = 2;
				gamestate = 0;
				Gdx.input.setInputProcessor(multiInput);

			}
		});
		difficultyMediumButtonResume.pad(80);

		difficultyHardButtonResume = new TextButton("RESUME", textButtonStyle);
		difficultyHardButtonResume.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {

				super.enter(event, x, y, pointer, fromActor);
				difficultyLevel = 3;
				gamestate = 0;
				Gdx.input.setInputProcessor(multiInput);

			}
		});
		difficultyHardButtonResume.pad(80);

		difficultyEasyButtonResume.setX(Gdx.graphics.getWidth() / 2
				- difficultyEasyButtonResume.getWidth() / 2);
		difficultyEasyButtonResume.setY(Gdx.graphics.getHeight() / 4);

		difficultyMediumButtonResume.setX(Gdx.graphics.getWidth() / 2
				- difficultyMediumButtonResume.getWidth() / 2);
		difficultyMediumButtonResume.setY(Gdx.graphics.getHeight() / 4);

		difficultyHardButtonResume.setX(Gdx.graphics.getWidth() / 2
				- difficultyHardButtonResume.getWidth() / 2);
		difficultyHardButtonResume.setY(Gdx.graphics.getHeight() / 4);

		easy = "DIFFICULTY IS NOW EASY";
		easyMessage = new Label(easy, messageStyle);
		medium = "DIFFICULTY IS NOW MEDIUM";
		mediumMessage = new Label(medium, messageStyle);
		hard = "DIFFICULTY IS NOW HARD";
		hardMessage = new Label(hard, messageStyle);

		easyMessage.setX(Gdx.graphics.getWidth() / 2 - easyMessage.getWidth()
				/ 2);
		easyMessage.setY(3 * Gdx.graphics.getHeight() / 4
				- easyMessage.getHeight());
		difficultyEasyStage.addActor(easyMessage);
		difficultyEasyStage.addActor(difficultyEasyButtonResume);

		mediumMessage.setX(Gdx.graphics.getWidth() / 2
				- mediumMessage.getWidth() / 2);
		mediumMessage.setY(3 * Gdx.graphics.getHeight() / 4
				- mediumMessage.getHeight());
		difficultyMediumStage.addActor(mediumMessage);
		difficultyMediumStage.addActor(difficultyMediumButtonResume);

		hardMessage.setX(Gdx.graphics.getWidth() / 2 - hardMessage.getWidth()
				/ 2);
		hardMessage.setY(3 * Gdx.graphics.getHeight() / 4
				- hardMessage.getHeight());
		difficultyHardStage.addActor(hardMessage);
		difficultyHardStage.addActor(difficultyHardButtonResume);

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
	
	public void easyMediumDispose(){
		easyMessage.remove();
		difficultyEasyButtonResume.remove();
		mediumMessage.remove();
		difficultyMediumButtonResume.remove();
	}
	
	public void mediumHardDispose(){
		mediumMessage.remove();
		difficultyMediumButtonResume.remove();
		hardMessage.remove();
		difficultyHardButtonResume.remove();
	}
	
	public void easyHardDispose(){
		easyMessage.remove();
		difficultyEasyButtonResume.remove();
		hardMessage.remove();
		difficultyHardButtonResume.remove();
	}

	@Override
	public void dispose() {
		world.dispose();
		map.dispose();
		level.dispose();
		HUDstage.dispose();
		debugger.dispose();
		mapRenderer.dispose();
		rayHandler.dispose();
		difficultyStage.dispose();
		settingsStage.dispose();
		difficultyEasyStage.dispose();
		difficultyMediumStage.dispose();
		difficultyHardStage.dispose();
		TextureManager.DisposeTextures();
		SoundManager.DisposeSounds();

	}

	public static World getPhysicsWorld() {
		// TODO Auto-generated method stub
		return world;
	}

	public static RayHandler getRayHandler() {
		// TODO Auto-generated method stub
		return rayHandler;
	}

	public static GameUI getUI() {
		return ui;
	}

	public static ArrayList<Body> bodiesToDestroy = new ArrayList<Body>();
	public static ArrayList<Body> bodiesToDeactivate = new ArrayList<Body>();
	public static ArrayList<Body> bodiesToActivate = new ArrayList<Body>();
	public static ArrayList<Light> lightsToActivate = new ArrayList<Light>();
	public static ArrayList<Light> lightsToDeactivate = new ArrayList<Light>();

	public static void destroyBody(Body b) {
		bodiesToDestroy.add(b);
	}

	private static void destroyBodies() {
		for (Body b : bodiesToDestroy) {
			if (b != null) {
				world.destroyBody(b);
			}
		}
		bodiesToDestroy.clear();
	}

	private static void deactivateBodies() {
		for (Body b : bodiesToDeactivate) {
			if (b != null) {
				b.setActive(false);
			}
		}
		bodiesToDeactivate.clear();
	}

	private static void activateBodies() {
		for (Body b : bodiesToActivate) {
			if (b != null) {
				b.setActive(true);
			}
		}
		bodiesToActivate.clear();
	}

	private static void deactivateLights() {
		for (Light b : lightsToDeactivate) {
			if (b != null) {
				b.setActive(false);
			}
		}
		lightsToDeactivate.clear();
	}

	private static void activateLights() {
		for (Light b : lightsToActivate) {
			if (b != null) {
				b.setActive(true);
			}
		}
		lightsToActivate.clear();
	}

	public static Stage getHUDStage() {
		return HUDstage;
	}

	public static OrthographicCamera getHUDCamera() {
		return HUDcamera;
	}

	public static Stage getLevelStage() {
		return level;
	}

	public static Group getPickUpGroup() {
		return pickUpGroup;
	}
	
	public static Group getEntities() {
		return entityGroup;
	}
	
	public static Group getProjectileGroup() {
		return projectileGroup;
	}

	public static Player getPlayer() {
		return player;
	}
	
	public static LevelDirector getDirector()
	{
		return director;
	}

	public static Body getGround() {
		return groundBody;
	}

	public static void hideTable(Table t) {
		t.setVisible(false);
	}

	public static void showTable(Table t) {
		t.setVisible(true);
	}
}
