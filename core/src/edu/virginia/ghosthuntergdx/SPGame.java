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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import edu.virginia.ghosthuntergdx.entities.*;

public class SPGame implements Screen,InputProcessor{

	GhostHunterGame game;
	static World world;
	
	Vector2 playerStartPos = new Vector2(10,5);
	OrthographicCamera camera;
	Player player;
	Stage level;
	private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();
	
	public SPGame(GhostHunterGame game)
	{
		this.game = game;
		world = new World(new Vector2(0,0),true);
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/Consts.BOX_TO_WORLD,Gdx.graphics.getHeight()/Consts.BOX_TO_WORLD);
		camera.setToOrtho(false);
		camera.update();
		player = new Player(playerStartPos);
		
		level = new Stage();
		level.setViewport(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera));
		level.addActor(player);
		Gdx.input.setInputProcessor(this);
		for(int i = 0; i < 5; i++)
		{
			touches.put(i, new TouchInfo());
		}
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
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
   
				boolean playerMoved = false;
				for(int i = 0; i < 5; i++)
				{
					if(touches.get(i).touched)
					{
						if(playerMoving && playerMovePointer == i)
						{
							Vector3 v = new Vector3(touches.get(i).touchX,touches.get(i).touchY,0);
							camera.unproject(v);
							v = v.scl(1/Consts.BOX_TO_WORLD);
							player.setMoveTarget(new Vector2(v.x,v.y));
							playerMoved = true;
						}else if(!playerMoving)
						{
						playerMovePointer = i;
						Vector3 v = new Vector3(touches.get(i).touchX,touches.get(i).touchY,0);
						camera.unproject(v);
						v = v.scl(1/Consts.BOX_TO_WORLD);
						player.setMoveTarget(new Vector2(v.x,v.y));
						playerMoving = true;
						}
					}
				}
				if(!playerMoved)
					playerMoving = false;
				
		 level.act(delta);
		 
		 level.draw();
				
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return true;
    }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
