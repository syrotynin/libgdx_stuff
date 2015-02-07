package com.mygdx.game.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.InputController;
import com.mygdx.game.LevelGenerator;
import com.mygdx.game.MyGame;

import entities.Jumper;

public class JumperScreen implements Screen {

	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private final float TIMESTEP  = 1/60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	Jumper jumper;
	Vector3 botLeft, botRight;
	LevelGenerator levelGenerator;
	
	@Override
	public void show() {
		if(Gdx.app.getType() == ApplicationType.Desktop)
			Gdx.graphics.setDisplayMode((int) (Gdx.graphics.getHeight() / 1.5f), Gdx.graphics.getHeight(), false);
		
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
		 
		botLeft = new Vector3(0, Gdx.graphics.getHeight(), 0);
		botRight = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
		
		camera.unproject(botRight);
		camera.unproject(botLeft);
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		// GROUND
		// body definition
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		// shape
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[] {new Vector2(botLeft.x, botLeft.y), new Vector2(botRight.x, botRight.y)});
		// fixture def (предопределение формы)
		fixtureDef.shape = groundShape;
		fixtureDef.friction = .5f; // 0-1 трение
		fixtureDef.restitution = 0; // на сколько уменьшается отскок в зависимости от пред.   (восстановление состояния)
		
		Body ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		
		// JUMPER
		jumper = new Jumper(world, 1, 1, 3);
		
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
		}, jumper)); // add jumper's input adapter
		
		groundShape.dispose();
		
		world.setContactFilter(jumper);
		world.setContactListener(jumper);
		
		// lvl generator
		levelGenerator = new LevelGenerator(ground, botLeft.x, botRight.x, jumper.width, jumper.height * 3,
				jumper.width * 1.5f, jumper.width * 2.5f, jumper.width / 3, 20 * MathUtils.degreesToRadians);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

		if(jumper.getBody().getPosition().x < botLeft.x)
			jumper.getBody().setTransform(botRight.x, jumper.getBody().getPosition().y, jumper.getBody().getAngle());
		else if(jumper.getBody().getPosition().x > botRight.x)
			jumper.getBody().setTransform(botLeft.x, jumper.getBody().getPosition().y, jumper.getBody().getAngle());
		
		jumper.update();
		
		//System.out.println("camera: "+ camera.position.y + " player:" + jumper.getBody().getPosition().y);
		
		camera.position.y = jumper.getBody().getPosition().y > camera.position.y ? jumper.getBody().getPosition().y : camera.position.y;
		camera.update();
		
		debugRenderer.render(world, camera.combined);
		
		levelGenerator.generate(camera.position.y + camera.viewportHeight / 2);
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
