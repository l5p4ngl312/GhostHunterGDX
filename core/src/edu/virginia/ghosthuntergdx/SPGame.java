package edu.virginia.ghosthuntergdx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import edu.virginia.ghosthuntergdx.entities.*;

public class SPGame implements Screen{

	GhostHunterGame game;
	static World world;
	
	Vector2 playerStartPos = new Vector2(10,5);
	OrthographicCamera camera;
	OrthographicCamera HUDcamera;
	
	Player player;
	Stage level;
	Stage HUDstage;
	
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	
	Box2DDebugRenderer debugger;
	
	public SPGame(GhostHunterGame game)
	{
		this.game = game;
	}
	
	int playerMovePointer = -1;
	boolean playerMoving = false;
	boolean debugPhysics = false;
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		world.step(1/45f, 6, 2);
		
        Gdx.gl.glClearColor(0.2f,0.2f,0.2f, delta);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        
        camera.position.set(player.getSprite().getX(), player.getSprite().getY(), 0);
        level.getCamera().update();
				
        
        float x = camera.position.x - camera.viewportWidth * camera.zoom;
        float y = camera.position.y - camera.viewportHeight * camera.zoom;

        float width = camera.viewportWidth * camera.zoom * 2;
        float height = camera.viewportHeight * camera.zoom * 2;

        mapRenderer.setView(camera.combined, x, y, width, height);
        mapRenderer.render();
        
        player.setMoveDir(new Vector2(mPad.getKnobPercentX(),mPad.getKnobPercentY()));
        player.setAttackDir(new Vector2(aPad.getKnobPercentX(),aPad.getKnobPercentY()));
		level.act(delta);
		 
		level.draw();
		
		if(debugPhysics)
			debugger.render(world, camera.combined.scale(Consts.BOX_TO_WORLD, Consts.BOX_TO_WORLD, 0));
		
        HUDstage.getCamera().update();
        HUDstage.act(delta);
        HUDstage.draw();	
	}

	@Override
	public void resize(int width, int height) {
		level.getViewport().update(width, height,true);
		HUDstage.getViewport().update(width, height,true);
	}

	Touchpad mPad;
	TouchpadStyle mStyle;
	Skin mSkin;
	
	Touchpad aPad;
	TouchpadStyle aStyle;
	Skin aSkin;
	
	int currentMap = 0;
	@Override
	public void show() {
		world = new World(new Vector2(0,0),true);
		//camera = new OrthographicCamera(Gdx.graphics.getWidth()/Consts.BOX_TO_WORLD,Gdx.graphics.getHeight()/Consts.BOX_TO_WORLD);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.setToOrtho(false);
		camera.update();
		
		HUDcamera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		HUDcamera.setToOrtho(false);
		HUDcamera.update();
		
		String path = "data/map"+currentMap+".tmx";
		
		map = new TmxMapLoader().load(path);
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		Physics.buildShapes(map,world,"WallCollisionLines");
		
		Vector2 startPos = playerStartPos;
		MapLayer spawnLayer = map.getLayers().get("PlayerSpawn");
		if(spawnLayer != null)
		{
			for(MapObject o : spawnLayer.getObjects())
			{
				if(o instanceof EllipseMapObject)
				{
					EllipseMapObject c = (EllipseMapObject)o;
					startPos = new Vector2(c.getEllipse().x/Consts.BOX_TO_WORLD,c.getEllipse().y/Consts.BOX_TO_WORLD);
				}
			}
		}
		player = new Player(startPos);
		
		mSkin = new Skin();
		mSkin.add("mBack", TextureManager.mBack);
		mSkin.add("mKnob", TextureManager.mKnob);
		
		mStyle = new TouchpadStyle();
		mStyle.knob = mSkin.getDrawable("mKnob");
		mStyle.background = mSkin.getDrawable("mBack");
		
		mPad = new Touchpad(10,mStyle);
		mPad.setBounds(Gdx.graphics.getWidth()-(250+Gdx.graphics.getWidth()/15), Gdx.graphics.getHeight()/15, 250, 250);
		
		aSkin = new Skin();
		aSkin.add("aBack",TextureManager.aBack);
		aSkin.add("aKnob",TextureManager.aKnob);
		
		aStyle = new TouchpadStyle();
		aStyle.knob = aSkin.getDrawable("aKnob");
		aStyle.background = aSkin.getDrawable("aBack");
		
		aPad = new Touchpad(10, aStyle);
		aPad.setBounds(Gdx.graphics.getWidth()/15, Gdx.graphics.getHeight()/15, 175, 175);
		
		Group entities = new Group();
		
		level = new Stage();
		level.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera));
		level.addActor(entities);
		
		HUDstage = new Stage();
		Gdx.input.setInputProcessor(HUDstage);
		
		HUDstage.addActor(mPad);
		HUDstage.addActor(aPad);
		HUDstage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),HUDcamera));
		
		entities.addActor(player);
		
		debugger = new Box2DDebugRenderer( true, true, true, true,true,true);
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
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
		
	}

	public static World getPhysicsWorld() {
		// TODO Auto-generated method stub
		return world;
	}


}
