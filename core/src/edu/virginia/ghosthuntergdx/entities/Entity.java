package edu.virginia.ghosthuntergdx.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

	public Vector2 position;
	public Vector2 velocity = new Vector2(0,0);
	public Vector2 scale = new Vector2(1,1);
	public Entity(Vector2 position)
	{
		this.position = position;
	}
	
	public abstract void update();
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();
	
}
