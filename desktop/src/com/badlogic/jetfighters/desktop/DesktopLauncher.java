package com.badlogic.jetfighters.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.jetfighters.JetFightersGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "JetFighters";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new JetFightersGame("Karlo"), config); // TODO read ID from command line args
    }
}
