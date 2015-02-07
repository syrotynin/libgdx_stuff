package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.AnimatedSprite;

public class Water extends Actor {

	private Animation animation;
	private AnimatedSprite animSprite;
	private float w, h;
	private float time;
	
	public Water(Animation animation, float w, float h) {
		animSprite = new AnimatedSprite(animation);
		this.animation = animation;
		this.w = w;
		this.h = h;
	}
	
	public void update() {
		time += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		update();
		batch.draw(animation.getKeyFrame(time), -w / 2, -h / 2, w, h);
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	
}
