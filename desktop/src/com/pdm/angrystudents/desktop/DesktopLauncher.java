package com.pdm.angrystudents.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pdm.angrystudents.AngryStudentsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280/2;
		config.height = 768/2;
		new LwjglApplication(new AngryStudentsGame(), config);
	}
}
