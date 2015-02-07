package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.InputController;
import com.mygdx.game.MyActor;
import com.mygdx.game.MyGame;

import entities.Car;

public class GameScreen2 implements Screen {
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private final float TIMESTEP  = 1/60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private static final float SPEED = 800;
	private Vector2 force = new Vector2();
	private Car car;
	
	Stage stage;
	
	@Override
	public void show() {
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
		
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), MyGame.getInstance().getBatch());
		stage.getViewport().setCamera(camera);
		
		Gdx.input.setInputProcessor(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				if(Keys.ESCAPE == keycode)
					MyGame.getInstance().showLoadScreen();
				
				return true;
			}
			
			@Override
			public boolean scrolled(int amount) {
				camera.zoom += amount/5f;
				return true;
			}
		});
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		// GROUND
				// body definition
				bodyDef = new BodyDef();
				bodyDef.type = BodyType.StaticBody;
				bodyDef.position.set(0, 0);
				// shape
				ChainShape groundShape = new ChainShape();
				groundShape.createChain(new Vector2[] {new Vector2(-50,0), new Vector2(50,0)} );
				// fixture def (предопределение формы)
				fixtureDef.shape = groundShape;
				fixtureDef.friction = .5f; // 0-1 трение
				fixtureDef.restitution = 0; // на сколько уменьшается отскок в зависимости от пред.   (восстановление состояния)
				
				world.createBody(bodyDef).createFixture(fixtureDef);
				
				groundShape.dispose();
		
		
		Sprite actorSprite = new Sprite(new Texture("img/peter.png"));
		actorSprite.setSize(2, 4);
		actorSprite.setOrigin(actorSprite.getWidth()/2, actorSprite.getHeight()/2);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		world.step(TIMESTEP, VELOCITYITERATIONS, VELOCITYITERATIONS);
		
		camera.update();
		
		stage.draw();
		stage.act(delta);
		
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 10;
		camera.viewportHeight = height / 10;
		
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
		MyGame.getInstance().updateBatch();
		dispose();
	}

	@Override
	public void dispose() {
		world.dispose();
		debugRenderer.dispose();
	}
}
