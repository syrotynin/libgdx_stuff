package com.mygdx.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class EyeListener extends InputListener {
	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		float x = event.getListenerActor().getX();
		float y = event.getListenerActor().getY();
		
		switch(keycode){
		case Keys.UP: 
			y += 10;
			break;
		case Keys.DOWN:
			y -= 10;
			break;
		case Keys.RIGHT:
			x += 10;
			break;
		case Keys.LEFT:
			x -= 10;
			break;
		}
		event.getListenerActor().setPosition(x, y);
		
		return true;
	}
}
