package com.mystaff.screeens;

import actors.Player;
import actors.Water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.mystaff.MyStaff;
import com.mystaff.utils.Box2DFactory;
import com.mystaff.utils.BuoyancyController;
import com.mystaff.utils.InputController;
import com.mystaff.utils.LvlGenerator;

public class GameScreen implements Screen, ContactListener, ContactFilter {

	/* Use Box2DDebugRenderer, which is a model renderer for debug purposes */
	private Box2DDebugRenderer debugRenderer;
	
	/* As always, we need a camera to be able to see the objects */
	private OrthographicCamera camera;

	// stage to draw pictures and handle actors
	private Stage stage;
	
	// Edges of screen
	public static float leftEdgeX;
	public static float rightEdgeX;
	
	/* Define a world to hold all bodies and simulate reactions between them */
	private World world;
	private BuoyancyController buoyancyController;
	
	// generates all environment stuff
	private LvlGenerator lvlGenerator;
	
	//game states
	GameState game_state;
	// current player
	private Player player;
	
	// water
	Water water;
	
	// water points to camera
	public static float waterLineY;
	private float waterPosY;
	private float waterHeight;
	
	@Override
	public void show() {
		world = new World(new Vector2(0, -9.81f), true);

		//create game state
		game_state = new GameState();
		
		/* Create renderer */
		debugRenderer = new Box2DDebugRenderer();

		/*
		 * Define camera viewport. Box2D uses meters internally so the camera
		 * must be defined also in meters. We set a desired width and adjust
		 * height to different resolutions.
		 */
		camera = new OrthographicCamera(20, 20 * (Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));
		
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), MyStaff.getInstance().getBatch());
		stage.getViewport().setCamera(camera);
		
		// init edges of screen
		leftEdgeX = -camera.viewportWidth/2;
		rightEdgeX = camera.viewportWidth/2 + 1;
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		// BOUNDS
		// body definition
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		// shape
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[] {
				new Vector2(leftEdgeX - 2, camera.viewportHeight/2),
				new Vector2(leftEdgeX - 2, -camera.viewportHeight/2),
				new Vector2(rightEdgeX, -camera.viewportHeight/2),
				new Vector2(rightEdgeX, camera.viewportHeight/2)});
		
		// fixture def (предопределение формы)
		fixtureDef.shape = groundShape;
		fixtureDef.friction = .5f; // 0-1 трение
		fixtureDef.restitution = 0; // на сколько уменьшается отскок в зависимости от пред.   (восстановление состояния)
		
		Body ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		
		groundShape.dispose();
		
		waterPosY = -(camera.viewportHeight / 2 - 1) / 2 - 1.5f;
		waterHeight = (camera.viewportHeight / 2 - 2) / 2;
		
		waterLineY = waterPosY + waterHeight;
		
		// Create the water 
		Shape shape = Box2DFactory.createBoxShape(
				rightEdgeX, waterHeight, new Vector2(0, 0), 0);
		fixtureDef = Box2DFactory.createFixture(shape, 1, 0.1f, 0,
				true);
		Body waterBody = Box2DFactory.createBody(world, BodyType.StaticBody,
				fixtureDef, new Vector2(0, waterPosY));

		// create water animation
		Animation waterAnimation = new Animation(0.7f, new TextureRegion(new Texture("img/water/water1.png")), 
				new TextureRegion(new Texture("img/water/water2.png")),
				new TextureRegion(new Texture("img/water/water3.png")), 
				new TextureRegion(new Texture("img/water/water4.png")));
		waterAnimation.setPlayMode(Animation.PlayMode.LOOP);
		
		water = new Water(waterAnimation, camera.viewportWidth + 1, waterHeight*2, waterPosY*1.5f);
		
		 /* Create a buoyancy controller using the previous body as a fluid
		 * sensor
		 */
		buoyancyController = new BuoyancyController(world, waterBody.getFixtureList().first());
		
		fixtureDef = Box2DFactory.createFixture(null, 0.4f, 0.5f, 0.5f, false);
		
		FixtureDef manDef = Box2DFactory.createFixture(null, 0.4f, 0.5f, 0.5f, false);
		
		// Player
		player = new Player(world, fixtureDef, manDef, 2, .5f, 0, 0);
		
		
		// lvl generator
		lvlGenerator = new LvlGenerator(world, stage);
		
		
		// INPUT PROCESSORS
		Gdx.input.setInputProcessor(new InputMultiplexer(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.ESCAPE:
					System.out.println("Escaped !");
					MyStaff.getInstance().showGameScreen();
					break;

				default: break;
				}
				return false;
			}
			

			
			@Override
			public boolean scrolled(int amount) {
				System.out.println("Scrolled ");
				camera.zoom += amount/5f;
				return false;
			}
		}, player.getPlayerInput())); //
		
		// world (water) contacts with other staff
		world.setContactListener(this);
		world.setContactFilter(this);
		
		// Add actors to stage
		stage.addActor(player);
		stage.addActor(water);
	}

	@Override
	public void render(float delta) {
		/* Clear screen with a black background */
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/* At first we draw stage -  than debug things */
		stage.draw();
		stage.act(delta);
		
		/* Render all graphics before do physics step */
		debugRenderer.render(world, camera.combined);
		
		
		
		/* Step the simulation with a fixed time step of 1/60 of a second */
		world.step(1 / 60f, 6, 2);
		// resolves game states
		//game_state.update(1 / 60f);
				
		player.update();
		
		// buoyancyController.step(1 / 60f);
		camera.update();

		buoyancyController.step();
		
		lvlGenerator.generate();
		
		gamePlay();
	}

	public void gamePlay(){
		if(player.getPosition().x <= leftEdgeX){
			System.out.println("Lost !");
			MyStaff.getInstance().showGameScreen();
		}
		
		
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		debugRenderer.dispose();
		world.dispose();

	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		//System.out.println("begin contact");
		
		if (fixtureA.isSensor()
				&& fixtureB.getBody().getType() == BodyType.DynamicBody) {
			//System.out.println("add body to BC");
			buoyancyController.addBody(fixtureB);
		} else if (fixtureB.isSensor()
				&& fixtureA.getBody().getType() == BodyType.DynamicBody) {
			//System.out.println("add body to BC");
			buoyancyController.addBody(fixtureA);
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		//System.out.println("end contact");
		
		if (fixtureA.isSensor()
				&& fixtureB.getBody().getType() == BodyType.DynamicBody) {
			//System.out.println("remove from BC");
			buoyancyController.removeBody(fixtureB);
		} else if (fixtureB.isSensor()
				&& fixtureA.getBody().getType() == BodyType.DynamicBody) {
			//System.out.println("remove from BC");
			if (fixtureA.getBody().getWorldCenter().y > -1) {
				buoyancyController.removeBody(fixtureA);
			}
		}
		
	}

	private void resolveShipToBird(Fixture bird, Fixture ship){
		bird.getBody().setUserData(LvlGenerator.TO_DELETE);
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Object userDataA = fixtureA.getUserData();
		Object userDataB = fixtureB.getUserData();
		
		System.out.println("A: " + userDataA + " B: " + userDataB);
		
		/*-------------------BIRDS and PLAYER----------------------*/
		if (userDataA != null && (String) userDataA == LvlGenerator.BIRD && 
				userDataB != null && (String) userDataB == Player.SHIP) {
			System.out.println("bird should collide");
			
			resolveShipToBird(fixtureA, fixtureB);
			return false;
		} else {
			if (userDataB != null && (String) userDataB == LvlGenerator.BIRD && 
					userDataA != null && (String) userDataA == Player.SHIP) {
				System.out.println("bird should collide");
				
				resolveShipToBird(fixtureB, fixtureB);
				return false;
			}
		}
		/*-------------------BIRDS and PLAYER----------------------*/
		
		return true;
	}
	
	public class GameState{
		private int LEVEL = 1;
		
		private static final int MIN_MAN_AGE = 1;
		private static final int MAX_MAN_AGE = 4;
		private int MAN_AGE = MIN_MAN_AGE;
		
		private float birds_in_sec;
		private float reefs_in_sec;
		private float time_since_last_bird = 0;
		private float time_since_last_reef = 0;
		
		private static final float MAX_HEALTH = 3;
		private float HEALTH = MAX_HEALTH;
		
		private int BEARD_POINTS = 0;
		
		private boolean LOST = false;

		public GameState() {
			resolveLevel(1); // first level default
		}
		
		// resolve level items
		private void resolveLevel(int lvl){
			switch(lvl){
			case 1:
				MAN_AGE = MIN_MAN_AGE;
				birds_in_sec = 2.5f;
				reefs_in_sec = 4f;
				time_since_last_bird = birds_in_sec/2;
				time_since_last_reef = reefs_in_sec/2;
				HEALTH = MAX_HEALTH;
				break;
			case 2:
				birds_in_sec = 0.3f;
				reefs_in_sec = 0.25f;
				time_since_last_bird = birds_in_sec/2;
				time_since_last_reef = reefs_in_sec/2;
				break;
			case 3:
				birds_in_sec = 0.3f;
				reefs_in_sec = 0.25f;
				time_since_last_bird = birds_in_sec/2;
				time_since_last_reef = reefs_in_sec/2;
				break;
			default:
				break;
			}
		}
		
		public void update(float time){
			
			should_generate(time);
		}
		
		public void levelUp(){
			LEVEL++;
			resolveLevel(LEVEL);
		}
		
		public int getLEVEL() {
			return LEVEL;
		}

		public void setLEVEL(int lEVEL) {
			LEVEL = lEVEL;
			resolveLevel(LEVEL);
		}

		public int getMAN_AGE() {
			return MAN_AGE;
		}

		public void increaseMAN_AGE() {
			MAN_AGE++;
			
			if(MAN_AGE > MAX_MAN_AGE)
				MAN_AGE = MIN_MAN_AGE;
		}
		
		public void setMAN_AGE(int mAN_AGE) {
			MAN_AGE = mAN_AGE;
		}

		// if should generate objects
		private void should_generate(float time){
			time_since_last_bird += time;
			time_since_last_reef += time;
			
			// should generate bird
				if(time_since_last_bird >= birds_in_sec){
					time_since_last_bird = 0;
					lvlGenerator.generateBird();
				}
			// should generate reef
				else if(time_since_last_reef >= reefs_in_sec){
					time_since_last_reef = 0;
					lvlGenerator.generateReef();
				}
		}

		public float getBirds_in_sec() {
			return birds_in_sec;
		}

		public void setBirds_in_sec(float birds_in_sec) {
			this.birds_in_sec = birds_in_sec;
		}

		public float getReefs_in_sec() {
			return reefs_in_sec;
		}

		public void setReefs_in_sec(float reefs_in_sec) {
			this.reefs_in_sec = reefs_in_sec;
		}

		public float getHEALTH() {
			return HEALTH;
		}

		public void setHEALTH(float hEALTH) {
			HEALTH = hEALTH;
		}

		public int getBEARD_POINTS() {
			return BEARD_POINTS;
		}

		public void setBEARD_POINTS(int bEARD_POINTS) {
			BEARD_POINTS = bEARD_POINTS;
		}

		public boolean isLOST() {
			return LOST;
		}

		public void setLOST(boolean lOST) {
			LOST = lOST;
		}

		public int getMinManAge() {
			return MIN_MAN_AGE;
		}

		public int getMaxManAge() {
			return MAX_MAN_AGE;
		}

		public float getTime_since_last_bird() {
			return time_since_last_bird;
		}

		public float getTime_since_last_reef() {
			return time_since_last_reef;
		}

		public float getMaxHealth() {
			return MAX_HEALTH;
		}
		
		
	}

}
