package edu.virginia.ghosthuntergdx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import edu.virginia.ghosthuntergdx.entities.*;

public class SPGame implements Screen{

	GhostHunterGame game;
	static World world;
	
	Vector2 playerStartPos = new Vector2(10,5);
	OrthographicCamera camera;
	Player player;
	Stage level;
	
	public SPGame(GhostHunterGame game)
	{
		this.game = game;
	}
	
	int playerMovePointer = -1;
	boolean playerMoving = false;
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		world.step(1/45f, 6, 2);
		
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        
        level.getCamera().update();
				
        
        player.setMoveDir(new Vector2(mPad.getKnobPercentX(),mPad.getKnobPercentY()));
        player.setAttackDir(new Vector2(aPad.getKnobPercentX(),aPad.getKnobPercentY()));
		level.act(delta);
		 
		level.draw();
				
	}

	@Override
	public void resize(int width, int height) {
		level.getViewport().update(width, height,true);
	}

	Touchpad mPad;
	TouchpadStyle mStyle;
	Skin mSkin;
	
	Touchpad aPad;
	TouchpadStyle aStyle;
	Skin aSkin;
	
	@Override
	public void show() {
		world = new World(new Vector2(0,0),true);
		
		//camera = new OrthographicCamera(Gdx.graphics.getWidth()/Consts.BOX_TO_WORLD,Gdx.graphics.getHeight()/Consts.BOX_TO_WORLD);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.setToOrtho(false);
		camera.update();
		player = new Player(playerStartPos);
		
		mSkin = new Skin();
		mSkin.add("mBack", TextureManager.mBack);
		mSkin.add("mKnob", TextureManager.mKnob);
		
		mStyle = new TouchpadStyle();
		mStyle.knob = mSkin.getDrawable("mKnob");
		mStyle.background = mSkin.getDrawable("mBack");
		
		mPad = new Touchpad(10,mStyle);
		mPad.setBounds(Gdx.graphics.getWidth()-(250+Gdx.graphics.getWidth()/15), Gdx.graphics.getHeight()/15, 250, 250);
		mPad.setZIndex(500);
		
		aSkin = new Skin();
		aSkin.add("aBack",TextureManager.aBack);
		aSkin.add("aKnob",TextureManager.aKnob);
		
		aStyle = new TouchpadStyle();
		aStyle.knob = aSkin.getDrawable("aKnob");
		aStyle.background = aSkin.getDrawable("aBack");
		
		aPad = new Touchpad(10, aStyle);
		aPad.setBounds(Gdx.graphics.getWidth()/15, Gdx.graphics.getHeight()/15, 175, 175);
		aPad.setZIndex(500);
		
		player.setZIndex(10);
		
		level = new Stage();
		Gdx.input.setInputProcessor(level);
		level.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera));
		level.addActor(mPad);
		level.addActor(aPad);
		level.addActor(player);
		
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
