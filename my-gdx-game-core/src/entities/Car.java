package entities;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.mygdx.game.MyGame;

public class Car extends InputAdapter{

	private static final float CORE_WIDTH_TO_WHEEL = 7;

	private static final float motorSpeed = 200;

	private static final float TORQUE_ON_DENSITY = 35;
	
	Body core, leftWheel, rightWheel;
	WheelJoint leftAxis, rightAxis;
	
	public Car(World world, FixtureDef coreDef, FixtureDef wheelDef, float width, float height, float x, float y){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		//core
		PolygonShape coreShape = new PolygonShape();
		coreShape.set(new Vector2[]{new Vector2(-width/2, -height/2), new Vector2(-width/2, 0), new Vector2(-width/4, height/2),
				new Vector2(width/4, height/2), new Vector2(width/2, 0), new Vector2(width/2, -height/2)});
		
		coreDef.shape = coreShape;
		core = world.createBody(bodyDef);
		core.createFixture(coreDef);
		
		//left wheel
		CircleShape wheelShape = new CircleShape();
		wheelShape.setRadius(width / CORE_WIDTH_TO_WHEEL);
		
		wheelDef.shape = wheelShape;
		
		leftWheel = world.createBody(bodyDef);
		leftWheel.createFixture(wheelDef);
		
		//right wheel
		rightWheel = world.createBody(bodyDef);
		rightWheel.createFixture(wheelDef);
		
		//left axis (ось)
		WheelJointDef axisDef = new WheelJointDef();
		axisDef.bodyA = core;
		axisDef.bodyB = leftWheel;
		axisDef.localAnchorA.set(-width/2 + wheelShape.getRadius(), -height/2 - wheelShape.getRadius()/2);
		axisDef.localAxisA.set(Vector2.Y); // (0,1)
		axisDef.frequencyHz = coreDef.density;
		axisDef.maxMotorTorque = coreDef.density * TORQUE_ON_DENSITY;
		
		leftAxis = (WheelJoint) world.createJoint(axisDef);
		
		//right axis
		axisDef.bodyB = rightWheel;
		axisDef.localAnchorA.set(width/2 - wheelShape.getRadius(), -height/2 - wheelShape.getRadius()/2);
		
		rightAxis = (WheelJoint) world.createJoint(axisDef);
	}
	
	public Body getCore(){
		return core;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.W:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(motorSpeed);
			break;
		case Keys.A:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(-motorSpeed);
			break;
		case Keys.S:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(-motorSpeed);
			break;
		case Keys.D:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(motorSpeed);
			break;
		default: break;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.W:
		case Keys.A:
		case Keys.S:
		case Keys.D:
			leftAxis.enableMotor(false);
			break;
		default: break;
		}
		return true;
	}
}
