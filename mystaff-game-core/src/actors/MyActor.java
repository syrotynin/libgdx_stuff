package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyActor extends Actor {
	Texture toDraw;
	Sprite sprite;
	Body body = null;
	
	public MyActor(Texture toDraw) {
		this.toDraw = toDraw;
		setSize(150,150);
		setPosition(100, 100);
	}
	
	public MyActor(Sprite sprite, Body body) {
		this.sprite = sprite;
		this.body = body;
		setSize(sprite.getWidth(), sprite.getHeight());
		setPosition(sprite.getX(), sprite.getY());
	}
	
	public MyActor(Texture toDraw, float width, float height, float x, float y){
		this.toDraw = toDraw;
		setSize(width,height);
		setPosition(x, y);
	}
	
	public MyActor(Texture toDraw, Body body, float width, float height){
		this.toDraw = toDraw;
		this.body = body;
		setSize(width,height);
		setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	public void draw(Batch batch, float parentAlpha) {
		if(sprite == null)
			batch.draw(toDraw, getX(), getY(), getWidth(), getHeight());
		else{
			sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
			sprite.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
			sprite.draw(batch);
		}
		
	}
}
