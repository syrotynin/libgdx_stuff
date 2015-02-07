package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mystaff.screeens.GameScreen;
import com.mystaff.utils.AnimatedSprite;

public class Water extends Actor {

	private Animation animation;
	private AnimatedSprite animSprite;
	private float w, h, bottomY;
	private float time;
	
	public Water(Animation animation, float w, float h, float bottomY) {
		animSprite = new AnimatedSprite(animation);
		this.animation = animation;
		this.w = w;
		this.h = h;
		this.bottomY = bottomY;
	}
	
	public void update() {
		time += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		update();
		batch.draw(animation.getKeyFrame(time), GameScreen.leftEdgeX, bottomY, w, h);
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	
}
