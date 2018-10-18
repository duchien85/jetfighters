package com.badlogic.jetfighters.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.jetfighters.JetFightersCore;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "jETFIGHTERS";
        config.width = 800;
        config.height = 480;

        String jetId = arg.length > 0 ? arg[0] : "Karlo";
        new LwjglApplication(new JetFightersCore(jetId), config);
    }
}
