package com.mygdx.game;

import java.awt.Event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class BlogicListener extends InputListener {
	
	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
			event.getListenerActor().setX(Gdx.graphics.getWidth() - x);
		
		return true;
	}
}
