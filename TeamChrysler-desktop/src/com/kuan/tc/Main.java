package com.kuan.tc;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TeamChrysler";
		cfg.useGL20 = true;
		cfg.width = 1920;
		cfg.height = 1104;
		cfg.resizable = true;
		
		new LwjglApplication(new TC(), cfg);
	}
}
