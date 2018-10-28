package com.badlogic.jetfighters.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.jetfighters.JetFightersGame;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DesktopLauncher {
    public static void main(String[] arg) throws UnknownHostException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "JetFighters";
        config.width = 1024;
        config.height = 768;
        String server = arg.length == 1 ? arg[0] : InetAddress.getLocalHost().getHostAddress();
        new LwjglApplication(new JetFightersGame(server), config); // TODO read ID from command line args
    }
}
