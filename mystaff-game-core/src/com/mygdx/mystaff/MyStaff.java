package com.mygdx.mystaff;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mystaff.screeens.GameScreen;

public class MyStaff extends Game {
	public final static String TITLE = "Sergey's app";
	
	private SpriteBatch batch;
	
	// screens
	GameScreen gameScreen;
	
	private static MyStaff instance = new MyStaff();
	public static MyStaff getInstance(){
		return instance;
	}
	
	private MyStaff(){} // private constructor
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		
		gameScreen = new GameScreen();
		
		setScreen(gameScreen);
	}
	
	public void showGameScreen(){
		setScreen(gameScreen);
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
	
	public void updateBatch(){
		batch = new SpriteBatch();
	}
}
