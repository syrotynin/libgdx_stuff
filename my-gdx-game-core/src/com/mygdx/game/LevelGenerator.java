package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class LevelGenerator {

	private Body environment;
	private float leftEdge, rightEdge, minGap, maxGap, minWidth, maxWidth, height, angle;
	private float y; // last generated platform coordinate
	
	
	
	public LevelGenerator(Body environment, float leftEdge, float rightEdge,
			float minGap, float maxGap, float minWidth, float maxWidth,
			float height, float angle) 
	{
		this.environment = environment;
		this.leftEdge = leftEdge;
		this.rightEdge = rightEdge;
		this.minGap = minGap; // difference in y coord between two platforms
		this.maxGap = maxGap; // 
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		this.height = height;
		this.angle = angle;
	}

	public void generate(float topEdge){
		if(y + MathUtils.random(minGap, maxGap) > topEdge)
			return;
		
		y = topEdge;
		float width = MathUtils.random(minWidth, maxWidth);
		float x = MathUtils.random(leftEdge, rightEdge - width);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2, height/2, new Vector2(x + width/2, y + height/2), MathUtils.random(-angle/2, angle/2));
		
		environment.createFixture(shape, 0);
		
		shape.dispose();
	}

	public Body getEnvironment() {
		return environment;
	}
	
}
