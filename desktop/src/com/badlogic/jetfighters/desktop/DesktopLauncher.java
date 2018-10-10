package com.badlogic.jetfighters.desktop;

import com.badlogic.jetfighters.JetFightersCore;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "jETFIGHTERS";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new JetFightersCore(), config);
    }
}
