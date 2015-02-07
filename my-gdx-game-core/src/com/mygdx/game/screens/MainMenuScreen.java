package com.mygdx.game.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;

public class MainMenuScreen implements Screen {

	private Stage stage;
	private Table table;
	private Skin skin;
	private TextureAtlas atlas;
	private BitmapFont font_white, font_black; 
	private TextButton buttonPlay, buttonExit;
	private Label heading;
	private int FPS;

	
	@Override
	public void show() {
		FPS = Gdx.graphics.getFramesPerSecond();
		
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), MyGame.getInstance().getBatch());
		
		Gdx.input.setInputProcessor(stage);
		
		// fonts
		font_black = new BitmapFont(Gdx.files.internal("fonts/black.fnt"));
		font_white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"));
		
		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);
		
		// buttons
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button.normal");
		style.down = skin.getDrawable("button.pressed");
		style.pressedOffsetX = 1;
		style.font = font_black;
		
		buttonPlay = new TextButton("Play", style);
		buttonPlay.pad(10);
		buttonPlay.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				MyGame.getInstance().showFirstScreen();
			}
		});
		
		buttonExit = new TextButton("Exit", style);
		buttonExit.pad(10);
		buttonExit.addListener(new ClickListener(){
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();
			}
		});
		
		//heading
		LabelStyle headingStyle = new LabelStyle(font_white, Color.WHITE);
		heading = new Label(MyGame.TITLE, headingStyle);
		heading.setFontScale(1.5f);
		
		// putting stuff together
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.add(heading);
		table.row();
		table.add(buttonPlay);
		table.row();
		table.add(buttonExit);
		
		table.getCell(heading).spaceBottom(150);
		table.getCell(buttonPlay).space(100);
		
		stage.addActor(table);
		
		//creating animations
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(FPS/2 == 0){
			Random rand = new Random();
			heading.setColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
			FPS = Gdx.graphics.getFramesPerSecond();
		}
		FPS--;
		
		stage.act(delta);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		font_white.dispose();
		font_black.dispose();
		skin.dispose();
		
	}

}
