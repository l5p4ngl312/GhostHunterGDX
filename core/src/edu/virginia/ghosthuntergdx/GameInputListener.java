package edu.virginia.ghosthuntergdx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.items.Item;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class GameInputListener extends Actor implements InputProcessor{

    public class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
        public String toString()
        {
        	return touchX+","+touchY;
        }
    }
    
    public GameInputListener()
    {
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
    }
    
    public static Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();
    
    int swapPointerA = 0;
    int swapPointerB = 0;
    float dropHoldTime = 0;
    public static final float timeToDrop = 0.4f;
    
    boolean aDown = false;
    @Override
    public void act(float dt)
    {
    	super.act(dt);
    	if(aDown)
    	{
    		dropHoldTime += dt;
    		if(dropHoldTime > timeToDrop)
    		{
                if(SPGame.getPlayer().primaryItem != null)
                {
                	SPGame.getPlayer().primaryItem.OnDropped(SPGame.getPlayer());
                	swapPointerA = -1;
                	dropHoldTime = 0;
                	aDown = false;
                }
    		}
    	}
    }
    
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
        	Vector3 touchLocation = new Vector3(screenX,screenY,0);
        	touchLocation = SPGame.getHUDCamera().unproject(touchLocation);
            touches.get(pointer).touchX = touchLocation.x;
            touches.get(pointer).touchY = touchLocation.y;
            touches.get(pointer).touched = true;
            
            if(SPGame.getPlayer().primaryItem != null)
            {
            	if(SPGame.getPlayer().primaryItem.icon.getBoundingRectangle().contains(new Vector2(touchLocation.x,touchLocation.y)))
            	{
            		swapPointerA = pointer;
            		aDown = true;
            	}
            }
            if(SPGame.getPlayer().secondaryItem != null)
            {
            	if(SPGame.getPlayer().secondaryItem.icon.getBoundingRectangle().contains(new Vector2(touchLocation.x,touchLocation.y)))
            	{
            		swapPointerB = pointer;
            	}
            }
        }
        return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
        	Vector3 touchLocation = new Vector3(screenX,screenY,0);
        	touchLocation = SPGame.getHUDCamera().unproject(touchLocation);
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
            if(pointer == swapPointerA)
            {
            	aDown = false;
            	dropHoldTime = 0;
            }
            
            if(SPGame.getPlayer().primaryItem != null)
            {
            	if(SPGame.getPlayer().primaryItem.icon.getBoundingRectangle().contains(new Vector2(touchLocation.x,touchLocation.y)) && pointer == swapPointerA)
            	{
            		Item temp = SPGame.getPlayer().secondaryItem;
            		SPGame.getPlayer().secondaryItem = SPGame.getPlayer().primaryItem;
            		SPGame.getPlayer().primaryItem = temp;
            		
            		if(SPGame.getPlayer().secondaryItem != null)
            			SPGame.getPlayer().secondaryItem.OnSlotSwap(SPGame.getPlayer());
            		if(SPGame.getPlayer().primaryItem != null)
            			SPGame.getPlayer().primaryItem.OnSlotSwap(SPGame.getPlayer());
            	}
            }
            if(SPGame.getPlayer().secondaryItem != null)
            {
            	if(SPGame.getPlayer().secondaryItem.icon.getBoundingRectangle().contains(new Vector2(touchLocation.x,touchLocation.y))&& pointer == swapPointerB)
            	{
            		Item temp = SPGame.getPlayer().secondaryItem;
            		SPGame.getPlayer().secondaryItem = SPGame.getPlayer().primaryItem;
            		SPGame.getPlayer().primaryItem = temp;
            		
            		if(SPGame.getPlayer().primaryItem != null)
            			SPGame.getPlayer().primaryItem.OnSlotSwap(SPGame.getPlayer());
            		if(SPGame.getPlayer().secondaryItem != null)
            			SPGame.getPlayer().secondaryItem.OnSlotSwap(SPGame.getPlayer());
            	}
            }
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
