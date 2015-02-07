package actors;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mystaff.screeens.GameScreen;
import com.mystaff.utils.Box2DFactory;

public class Player extends Actor {
	public final static String SHIP = "SHIP";
	
	private Body boat, man;
	private Joint joint;
	private Vector2 velocity = new Vector2();
	private float movementForce = 4, jumpForce = 3.5f;
	private float streamForce = -.65f; // ship moving to left edge of screen
	private float WIDTH;
	private float HEIGHT;
	
	//sprites and paths to pics
	private Sprite boatSprite;
	private Sprite manSprite;
	public final static String boatPicPath = "img/ship.png";
	public final static String manPicPath = "img/guy12.png";

	// Ruslan's boat coefficients
	private float CORE_PART = 3.45f;
	private float MAN_SIZE = 2f;
	
	private PlayerInput playerInput;
//-------------------------------------nested class---------------------//
	class PlayerInput extends InputAdapter{
		@Override
		public boolean keyDown(int keycode) {
			switch(keycode){
			case Keys.A:
				//boat.applyLinearImpulse(-movementForce, 0, boat.getWorldCenter().x ,boat.getWorldCenter().y, true);
				velocity.x -= movementForce;
				break;
			case Keys.D:
				//boat.applyLinearImpulse(movementForce, 0, boat.getWorldCenter().x ,boat.getWorldCenter().y, true);
				velocity.x += movementForce;
				break;
			case Keys.SPACE:
				System.out.println(GameScreen.waterLineY + " " + boat.getPosition().y);
				if(boat.getPosition().y - .5f <= GameScreen.waterLineY) // let the boat jump only if it is in the water
				boat.applyLinearImpulse(0, jumpForce, boat.getWorldCenter().x ,boat.getWorldCenter().y, true);
				break;
			default: return false;
			}
			return true;
		}
		
		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Keys.A || keycode == Keys.D || keycode == Keys.SPACE){
				velocity.x = 0;
				velocity.y = 0;  // NOTE: 'velocity = Vector2.Zero' doesn't work
			}
			else
				return false;
			return true;
		}
	}
	//-------------------------------------nested class---------------------//
	
	public Player(World world, FixtureDef boatDef, FixtureDef menDef, float width, float height, float x, float y) {
		WIDTH = width;
		HEIGHT = height;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		playerInput = new PlayerInput();
		
		// boat
		Shape boatShape = Box2DFactory.createPolygonShape(new Vector2[]{ // creating ugly Ruslan's boat 
				new Vector2(-width/2, height/2), new Vector2(-width/2 + width*0.046f, -height/2),
				new Vector2(width/2 - width*0.17f, -height/2), new Vector2(width/2, height/2)});
		boatDef.shape = boatShape;
			
		boat = world.createBody(bodyDef);
		boat.createFixture(boatDef).setUserData(SHIP); // SHIP mark for collisions
		
		// men
		Shape menShape = Box2DFactory.createBoxShape(width/8, height/2, new Vector2(x, y) , 0);
		menDef.shape = menShape;
		
		man = world.createBody(bodyDef);
		man.createFixture(menDef).setUserData(SHIP);
		
		WeldJointDef jointDef = new WeldJointDef();
		jointDef.bodyA = boat;
		jointDef.bodyB = man;
		jointDef.localAnchorB.y = -height;
		
		joint = world.createJoint(jointDef);
		
		//dispose stuff
		//boatShape.dispose();
		menShape.dispose();
		boatShape.dispose();
		
		// Sprite generation
		boatSprite = new Sprite(new Texture(boatPicPath));
		boatSprite.setSize(width, height * CORE_PART); // 3.45 - core part to sail (1 : 3.45)
		boatSprite.setOrigin(boatSprite.getWidth()/2, boatSprite.getHeight()/2/CORE_PART);
		
		manSprite = new Sprite(new Texture(manPicPath));
		manSprite.setSize(width/4, height*MAN_SIZE);
		//manSprite.setOrigin(manSprite.getWidth()/2, manSprite.getHeight()/2);
		manSprite.setOrigin(width/8 + width/3, height*MAN_SIZE/2 - HEIGHT/2);
	}
	
	public void update(){	
		boat.applyForceToCenter(velocity.x + streamForce, velocity.y, true);
		
		//System.out.println("x: "+boat.getLinearVelocity().x + " y: " +boat.getLinearVelocity().y);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//man
				//manSprite.setPosition(man.getPosition().x - manSprite.getWidth()/2, man.getPosition().y - manSprite.getHeight()/2);
				manSprite.setPosition(man.getPosition().x - manSprite.getWidth()/2 - boatSprite.getWidth()/3,
									man.getPosition().y - manSprite.getHeight()/2 + HEIGHT/2);
				manSprite.setRotation(man.getAngle()*MathUtils.radiansToDegrees);
				manSprite.draw(batch);
		// boat
				boatSprite.setPosition(boat.getPosition().x - boatSprite.getWidth()/2, boat.getPosition().y - boatSprite.getHeight()/2 / CORE_PART);
				boatSprite.setRotation(boat.getAngle()*MathUtils.radiansToDegrees);
				boatSprite.draw(batch);
	}
	
	public Vector2 getPosition(){
		return boat.getPosition();
	}

	public float getWIDTH() {
		return WIDTH;
	}

	public float getHEIGHT() {
		return HEIGHT;
	}

	public PlayerInput getPlayerInput() {
		return playerInput;
	}
	
	
}
