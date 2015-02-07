package com.mygdx.game.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.MyGame;

public class SpriteAccessor implements TweenAccessor<Sprite> {

	public static final int ALPHA = 0;
	
	@Override
	public int getValues(Sprite target, int tweenType, float[] sendVal) {
		switch(tweenType){
			case ALPHA:
				sendVal[0] = target.getColor().a;
				return 1;
			default:
				assert false;
				return 0;
		}
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] sendVal) {
		switch(tweenType){
			case ALPHA:
				target.setColor(target.getColor().a, target.getColor().g, target.getColor().b, sendVal[0]);
				break;
			default:
				assert false;
		}	
	}

}
