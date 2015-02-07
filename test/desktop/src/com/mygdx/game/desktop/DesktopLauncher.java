package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGame;
import com.mygdx.game.screens.FirstScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = MyGame.TITLE;
		config.width = 640;
		config.height = 480;
		
		new LwjglApplication(MyGame.getInstance(), config);
	}
}