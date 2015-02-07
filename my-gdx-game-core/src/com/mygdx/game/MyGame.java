package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.FirstScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.JumperScreen;
import com.mygdx.game.screens.LoadScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.SecondScreen;

public class MyGame extends Game {
	public final static String EYE = "EYE";
	public final static String BLOGIC = "BLOGIC";
	public final static String TITLE = "Sergey's app";
	
	private LoadScreen loadScreen;
	private SecondScreen secondScreen;
	private FirstScreen firstScreen;
	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	private JumperScreen jumperScreen;
	private SpriteBatch batch;
	
	private static MyGame instance = new MyGame();
	
	private MyGame(){} // private constructor
	
	public static MyGame getInstance(){
		return instance;
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		
		mainMenuScreen = new MainMenuScreen();
		firstScreen  = new FirstScreen();
		secondScreen = new SecondScreen(EYE, batch);
		loadScreen = new LoadScreen();
		gameScreen = new GameScreen();
		jumperScreen = new JumperScreen();
		
		setScreen(loadScreen);
		//setScreen(gameScreen);
	}
	
	public void showMenu(){
		setScreen(mainMenuScreen);
	}
	
	public void showFirstScreen(){
		setScreen(firstScreen);
	}
	
	public void showLoadScreen(){
		setScreen(loadScreen);
	}
	
	public void showSecondScreen(String type){
		secondScreen = new SecondScreen(EYE, batch);
		if(type.equals(BLOGIC))
			secondScreen = new SecondScreen(BLOGIC, batch);
		
		setScreen(secondScreen);
	}
	
	public void showGameScreen(){
		setScreen(gameScreen);
	}
	
	public void showJumperScreen(){
		setScreen(jumperScreen);
	}
	
	public void updateBatch(){
		batch = new SpriteBatch();
	}
}
