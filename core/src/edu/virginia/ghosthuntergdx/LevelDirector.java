package edu.virginia.ghosthuntergdx;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.entities.Ghost;
import edu.virginia.ghosthuntergdx.entities.Zombie;
import edu.virginia.ghosthuntergdx.items.Ammo;
import edu.virginia.ghosthuntergdx.items.Artifact;
import edu.virginia.ghosthuntergdx.items.Flashlight;
import edu.virginia.ghosthuntergdx.items.Pistol;
import edu.virginia.ghosthuntergdx.items.Weapon.ammoType;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class LevelDirector extends Actor{

	public int difficultyLevel = 1;
	public int playerProgress = 1;
	TiledMap map;
	
	ArrayList<Rectangle> spawnArea;
	Random rand;
	public LevelDirector(int difficultyLevel, TiledMap map)
	{
		this.difficultyLevel = difficultyLevel;
		this.playerProgress = playerProgress;
		this.map = map;
		MapObjects objects = map.getLayers().get("MobSpawnLayer").getObjects();
		rand = new Random();
		spawnArea = new ArrayList<Rectangle>();
		for(MapObject o : objects)
		{
			if(o instanceof RectangleMapObject)
			{
				RectangleMapObject r = (RectangleMapObject)o;
				Rectangle rect = r.getRectangle();
				rect.setWidth(rect.getWidth()/Consts.BOX_TO_WORLD);
				rect.setHeight(rect.getHeight()/Consts.BOX_TO_WORLD);
				rect.setX(rect.getX()/Consts.BOX_TO_WORLD);
				rect.setY(rect.getY()/Consts.BOX_TO_WORLD);
				Gdx.app.debug("Spawn rect", rect.toString());
				spawnArea.add(rect);
			}
		}
		
		MapProperties prop = map.getProperties();

		int mWidth = prop.get("width", Integer.class);
		int mHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		mapWidth = (int) ((mWidth * tilePixelWidth)/Consts.BOX_TO_WORLD);
		mapHeight = (int) ((mHeight * tilePixelHeight)/Consts.BOX_TO_WORLD);
		
		Gdx.app.debug("Map width",mapWidth+"");
		Gdx.app.debug("Map Height",mapHeight+"");
		spawnNewObject(ObjectType.Flashlight);
		spawnNewObject(ObjectType.Pistol);
		setVariables();
		for(int i = 0; i < maxEnemies; i++)
		{
			spawnNewObject(ObjectType.Zombie);
		}
	}
	int mapWidth;
	int mapHeight;
	//Maximum number of enemies allowed on the level
	int maxEnemies;
	
	
	float spawnTime;
	float spawnTimer = 0;
	
	float ammoSpawnTimer;
	float ammoSpawnTimeFactor = 20;
	float ammoSpawnTime = (float) (Math.random()*ammoSpawnTimeFactor);
	
	int decoyArtifacts;
	
	public void setDifficulty(int difficultyLevel)
	{
		this.difficultyLevel = difficultyLevel;
		setVariables();
	}
	public void playerProgressed()
	{
		playerProgress++; 
		setVariables();
	}
	private void setVariables()
	{
		switch(difficultyLevel)
		{
		case 1:
			maxEnemies = 7*playerProgress;
			decoyArtifacts = 2;
			spawnTime = 15;
			ammoSpawnTimeFactor = 90;
			break;	
		case 2:
			maxEnemies = 10*playerProgress;
			spawnTime = 10;
			decoyArtifacts = 3;
			ammoSpawnTimeFactor = 120;
			break;	
		case 3:
			maxEnemies = 12*playerProgress;
			spawnTime = 5;
			decoyArtifacts = 5;
			ammoSpawnTimeFactor = 240;
		}
	}
	
	@Override
	public void act(float delta)
	{
		spawnTimer += delta;
		ammoSpawnTimer+=delta;
		int enemyCount = 0;
		for(Actor a : SPGame.getEntities().getChildren())
		{
			if(a instanceof Zombie)
			{
				enemyCount++;
			}
		}
		
		if(spawnTimer > spawnTime && enemyCount < maxEnemies)
		{
			spawnNewObject(ObjectType.Zombie);
			spawnTimer = 0;
		}
		
		if(ammoSpawnTimer > ammoSpawnTime)
		{
			spawnNewObject(ObjectType.Ammo);
			ammoSpawnTimer = 0;
			ammoSpawnTime = (float)Math.random()*ammoSpawnTimeFactor;
		}
		
		enemyCount = 0;
		for(Actor a : SPGame.getEntities().getChildren())
		{
			if(a instanceof Ghost)
			{
				enemyCount++;
			}
		}
		if(enemyCount < playerProgress)
		{
			spawnNewObject(ObjectType.Ghost);
		}
		
		enemyCount = 0;
		for(Actor a : SPGame.getEntities().getChildren())
		{
			if(a instanceof Artifact)
			{
				Artifact art = (Artifact)a;
				if(art.associatedGhost == null)
				{
				enemyCount++;
				}
			}
		}
		
		if(enemyCount < decoyArtifacts)
		{
			spawnNewObject(ObjectType.Artifact);
		}
	}
	
	private enum ObjectType{Ghost,Zombie,Flashlight,Pistol,Ammo,Artifact};
	
	private Actor spawnNewObject(ObjectType t)
	{
		boolean success = false;
		Vector2 spawnPos = Vector2.Zero;
		while(!success)
		{
			float x = mapWidth*rand.nextFloat();
			float y = mapHeight*rand.nextFloat();
			spawnPos = new Vector2(x,y);
			
			if(SPGame.getPlayer().getBody().getPosition().sub(spawnPos).len() < 8f)
			{
				continue;
			}
			
			for(Rectangle r : spawnArea)
			{
				if(r.contains(spawnPos))
				{
					success = true;
					break;
				}
			}
		}
		
		if(t == ObjectType.Ghost)
		{
			Ghost g = new Ghost(spawnPos);
			SPGame.getEntities().addActor(g);
			Artifact a = (Artifact)spawnNewObject(ObjectType.Artifact);
			a.associatedGhost = g;
			g.associatedArtifact = a;
			return g;
		}
		
		if(t == ObjectType.Zombie)
		{
			Zombie z = new Zombie(spawnPos);
			SPGame.getEntities().addActor(z);
			return z;
		}
		
		if(t == ObjectType.Ammo)
		{
			Ammo a = new Ammo(spawnPos, ammoType.PISTOL);
			SPGame.getPickUpGroup().addActor(a);
			return a;
		}
		
		if(t == ObjectType.Flashlight)
		{
			Flashlight f = new Flashlight(spawnPos);
			SPGame.getPickUpGroup().addActor(f);
			return f;
		}
		
		if(t == ObjectType.Pistol)
		{
			Pistol p = new Pistol(spawnPos,9);
			SPGame.getPickUpGroup().addActor(p);
			return p;
		}
		
		if(t == ObjectType.Artifact)
		{
			Artifact a = new Artifact(spawnPos);
			SPGame.getEntities().addActor(a);
			return a;
		}
		
		return null;
	}
	
}
