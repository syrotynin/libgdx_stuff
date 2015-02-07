package entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Jumper extends InputAdapter implements ContactFilter, ContactListener {
	private Body body;
	private Fixture fixture;
	public final float width, height;
	private Vector2 velocity = new Vector2();
	private float movementForce = 500, jumpForce = 170;
	
	public Jumper(World world, float x, float y, float width){
		this.width = width;
		height = width/2;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.fixedRotation = true;
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(height/2, width/2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.restitution = 0;
		fixtureDef.friction = .8f;
		fixtureDef.density = 3;
		
		body = world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		
	}

	public void update(){	
		body.applyForceToCenter(velocity, true);
		
		System.out.println(body.getLinearVelocity().y);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.A:
			velocity.x -= movementForce;
			break;
		case Keys.D:
			velocity.x += movementForce;
			break;
		case Keys.SPACE:
			body.applyLinearImpulse(0, 200,body.getWorldCenter().x ,body.getWorldCenter().y, true);
		default: return false;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.A || keycode == Keys.D)
			velocity.x = 0;
		else
			return false;
		return true;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getMovementForce() {
		return movementForce;
	}

	public void setMovementForce(float movementForce) {
		this.movementForce = movementForce;
	}

	public Body getBody() {
		return body;
	}

	public Fixture getFixture() {
		return fixture;
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		if(fixture == fixtureA || fixture == fixtureB){
			//System.out.println(body.getLinearVelocity().y + " " + jumpForce/10);
			return body.getLinearVelocity().y < 0; // false if jumper goes up
		}
		return false;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		if(contact.getFixtureA() == fixture || contact.getFixtureB() == fixture)
			if(contact.getWorldManifold().getPoints()[0].y <= body.getPosition().y - height/2){
				body.applyLinearImpulse(0, jumpForce, body.getWorldCenter().x, body.getWorldCenter().y, true);
			}
		
	}
	
	
}
