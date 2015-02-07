package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.BlogicListener;
import com.mygdx.game.EyeListener;
import com.mygdx.game.MyActor;
import com.mygdx.game.MyGame;

public class SecondScreen implements Screen {
	private Stage stage;
	private String type;
	private MyActor actor;
	
	public SecondScreen(String type, SpriteBatch batch) {
		this.type = type;
		
		boolean flag = false;
		switch(type){
			case MyGame.EYE:
				actor = new MyActor(new Texture("img/eye.jpg"));
				actor.addListener(new EyeListener());
				flag = true;
				break;
			case MyGame.BLOGIC:
				actor = new MyActor(new Texture("img/badlogic.jpg"));
				actor.addListener(new BlogicListener());
				break;
			default: 
				break;
		}
		
		actor.addListener(new BackListener());
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
		stage.addActor(actor);
		
		if(flag)
			stage.setKeyboardFocus(actor);
	}

	class BackListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			MyGame.getInstance().showFirstScreen();
		}
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
		
	}
	
	
}
