package com.mystaff.utils;

import actors.MyActor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mystaff.screeens.GameScreen;

public class LvlGenerator {

	public final static String BIRD = "BIRD";
	public final static String REEF = "REEF";
	
	public final static String TO_DELETE = "toDelete";
	
	private World world;
	Stage stage;
	
	//paths to pics
		public final static String reefPicPath = "img/guy12.png";
		public final static String birdicPath = "img/guy12.png";
	
	//reefs
	private int MAX_REEFS = 1;
	private float maxReefWidth = .85f;
	private float minReefWidth = .45f;
	private float maxReefHeight = 1.15f;
	private float minReefHeight = .75f;
	private Array<Body> reefsArray = new Array<Body>();
	public static final float reefSpeed = -3; 
	
	// birds
	private int MAX_BIRDS = 1;
	private float maxBirdWidth = .65f;
	private float minBirdWidth = .4f;
	private float maxBirdHeight = 0.15f;
	private float minBirdHeight = .1f;
	private Array<Body> birdsArray = new Array<Body>();
	public static final float birdSpeed = -3.5f; 
	
	public LvlGenerator(World world, Stage stage) {
		this.world = world;
		this.stage = stage;
	}
	
	public void generate(){
		
		if(reefsArray.size < MAX_REEFS)
			generateReef();
		
		if(birdsArray.size < MAX_BIRDS)
			generateBird();
		
		update();
	}
	
	private void update(){
		if(reefsArray.size > 0)
			updateReefs();
		if(birdsArray.size > 0)
			updateBirds();
	}

	private void generateObj(float width, float height, Array<Body> arr, float startLine, Object userData){
		PolygonShape shape = new PolygonShape();
		BodyDef bodyDef = new BodyDef();
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(GameScreen.rightEdgeX - width/2, GameScreen.waterLineY + height/2 + startLine);
		
		shape.setAsBox(width/2, height/2);
		
		Body body = world.createBody(bodyDef);
		body.createFixture(shape, 0).setUserData(userData);
		arr.add(body);
		
		Sprite sprite = new Sprite(new Texture(reefPicPath));
		sprite.setSize(width, height);
		MyActor actor = new MyActor(sprite, body);
		stage.addActor(actor);
		
		shape.dispose();
	}
	
	public void generateReef(){
		float width = MathUtils.random(minReefWidth, maxReefWidth); 
		float height = MathUtils.random(minReefHeight, maxReefHeight);
		//System.out.println("(reef) w: " + width + " h: " + height);
		
		generateObj(width, height, reefsArray, MathUtils.random(-.4f, -.2f), REEF);
	}
	
	public void generateBird(){
		float width = MathUtils.random(minBirdWidth, maxBirdWidth); 
		float height = MathUtils.random(minBirdHeight, maxBirdHeight);
		//System.out.println("(bird) w: " + width + " h: " + height);
		
		generateObj(width, height, birdsArray, MathUtils.random(1.5f, 2.6f), BIRD);
	}

	private void updateReefs(){
		for(Body body : reefsArray){
			if(body.getPosition().x < GameScreen.leftEdgeX){
				removeReef(body);
				return;
			}
			
			body.setLinearVelocity(reefSpeed, 0);
		}
	}
	
	private void updateBirds() {
		for(Body body : birdsArray){
			if((body.getPosition().x < GameScreen.leftEdgeX) || (String) body.getUserData() == TO_DELETE){
				removeBird(body);
				return;
			}
			
			body.setLinearVelocity(birdSpeed, 0);
		}
	}
	
	public void removeBird(Body body){
		birdsArray.removeValue(body, true);
		world.destroyBody(body);
	}
	
	
	public void removeReef(Body body){
		reefsArray.removeValue(body, true);
		world.destroyBody(body);
	}
	
	public int getMAX_REEFS() {
		return MAX_REEFS;
	}

	public void setMAX_REEFS(int mAX_REEFS) {
		MAX_REEFS = mAX_REEFS;
	}

	public int getReefs() {
		return reefsArray.size;
	}
}
