package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LevelDirector extends Actor{

	public int difficultyLevel = 1;
	public int playerProgress = 1;
	TiledMap map;
	
	public LevelDirector(int difficultyLevel, int playerProgress, TiledMap map)
	{
		this.difficultyLevel = difficultyLevel;
		this.playerProgress = playerProgress;
		this.map = map;
	}
	//Maximum number of enemies allowed on the level
	int maxEnemies;
	
	float minSpawnDistance;
	
	float respawnTimer;
		
	
	@Override
	public void act(float delta)
	{
		
	}
	
}
