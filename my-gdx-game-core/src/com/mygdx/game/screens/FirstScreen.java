package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;

public class FirstScreen implements Screen {
	private Texture img;
	private Texture eye;
	private TextureRegion tr;
	private Stage stage;
	private Group group;
	
	class EyeAct extends Actor{
		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(eye, getX(), getY(), getWidth(), getHeight());
		}
		
	}
	
	class BadLogicAct extends Actor{
		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(tr, getX(), getY(), getWidth(), getHeight());
		}
		
	}
	
	class PicListener extends ClickListener{
		private String type;
		
		public PicListener(String type) {
			this.type = type;
		}
		
		@Override
		public void clicked(InputEvent event, float x, float y) {
			MyGame.getInstance().showSecondScreen(type);
		}
	}

	public void initActors(){
		boolean flag = true;
		
		for(int i = 0; i < 8; i++){
			Actor actor = new Actor();
			if(flag){
				actor = new EyeAct();
				actor.addListener(new PicListener(MyGame.EYE));
			}
			else{
				actor = new BadLogicAct();
				actor.addListener(new PicListener(MyGame.BLOGIC));
			}
			
			actor.setSize(i*20, i*15);
			actor.setPosition(i*60, i*50);
			
			
			group.addActor(actor);
			flag = !flag;
		}
	}
	

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		img = new Texture("img/badlogic.jpg");
		eye = new Texture("img/eye.jpg");
		
		tr = new TextureRegion(img, 150, 150, 64, 64);
		
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), MyGame.getInstance().getBatch());
		
		group = new Group();
		group.setSize(640, 640);
		group.setPosition(0, 50);
		
		initActors();
		
		stage.addActor(group);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		stage.act(delta);
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
		img.dispose();
		eye.dispose();
		
	}
	
}
