package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.AnimatedSprite;
import com.mygdx.game.InputController;
import com.mygdx.game.MyActor;
import com.mygdx.game.MyGame;

import entities.Car;
import entities.Water;

public class GameScreen implements Screen {

	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private final float TIMESTEP  = 1/60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private Body box;
	private Vector2 force = new Vector2();
	private Car car;
	Water water;
	
	Stage stage;
	private MyActor peter;
	
	@Override
	public void show() {
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
		
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), MyGame.getInstance().getBatch());
		stage.getViewport().setCamera(camera);
		
		// water animation
		Animation animation = new Animation(0.4f, new TextureRegion(new Texture("img/water/w1.png")), 
				new TextureRegion(new Texture("img/water/w2.png")),
				new TextureRegion(new Texture("img/water/w3.png")), 
				new TextureRegion(new Texture("img/water/w4.png")));
		animation.setPlayMode(Animation.PlayMode.LOOP);
		
		water = new Water(animation, camera.viewportWidth, camera.viewportHeight);
		stage.addActor(water);
		////////////////
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		//BOX
				// body definition
				bodyDef = new BodyDef();
				bodyDef.type = BodyType.DynamicBody;
				bodyDef.position.set(2, 10);
				// shape
				PolygonShape boxShape = new PolygonShape();
				boxShape.setAsBox(1, 1.5f);
				// fixture def (предопределение формы)
				fixtureDef.shape = boxShape;
				fixtureDef.friction = .75f; // 0-1 трение
				fixtureDef.restitution = .1f; // на сколько уменьшается отскок в зависимости от пред.   (восстановление состояния)
				fixtureDef.density = 5;
				
				box = world.createBody(bodyDef);
				box.createFixture(fixtureDef);
				
				boxShape.dispose();
		
		// BALL + BOX
				// shape def
				CircleShape circle = new CircleShape();
				circle.setPosition(new Vector2(0,2));
				circle.setRadius(1.5f);
				// fixture def (предопределение формы)
				fixtureDef.shape = circle;
				fixtureDef.density = 2.5f; // kg in 1 sqere meter
				fixtureDef.friction = .25f; // 0-1 трение
				fixtureDef.restitution = 0.6f; // на сколько уменьшается отскок в зависимости от пред.   (восстановление состояния)
				
				box.createFixture(fixtureDef);
				
				circle.dispose();
		
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
				
				Body ground = world.createBody(bodyDef);
				ground.createFixture(fixtureDef);
				
				groundShape.dispose();
		
		box.applyAngularImpulse(10, true);
		
		Sprite actorSprite = new Sprite(new Texture("img/peter.png"));
		actorSprite.setSize(2, 4);
		actorSprite.setOrigin(actorSprite.getWidth()/2, actorSprite.getHeight()/2);
		
		peter = new MyActor(actorSprite, box);
		stage.addActor(peter);
		box.setUserData(peter);
		
		// Car test
		fixtureDef.friction = .4f;
		fixtureDef.restitution = .2f;
		fixtureDef.density = 5;
		
		FixtureDef wheelFixtureDef = new FixtureDef();
		wheelFixtureDef.density = fixtureDef.density + 1;
		wheelFixtureDef.restitution = .3f; 
		wheelFixtureDef.friction = 100;
		
		car = new Car(world, fixtureDef, wheelFixtureDef, 4, 2, 14, 1);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.ESCAPE:
					MyGame.getInstance().showLoadScreen();
					break;

				default: break;
				}
				return false;
			}
			

			
			@Override
			public boolean scrolled(int amount) {
				camera.zoom += amount/5f;
				return false;
			}
		}, car)); // add car's input adapter
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		//box.applyForceToCenter(force, true);
		car.getCore().applyForceToCenter(force, true);
		
		//camera.position.set(box.getPosition().x, box.getPosition().y, 0);
		//camera.position.set(car.getCore().getPosition().x, car.getCore().getPosition().y, 0);
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
