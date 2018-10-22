package com.badlogic.jetfighters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.jetfighters.client.network.UdpClient;
import com.badlogic.jetfighters.screens.MainMenuScreen;
import com.google.common.eventbus.EventBus;

public class JetFightersGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    private String jetId;
    public static EventBus eventBus = new EventBus();
    public UdpClient client = new UdpClient(this, eventBus);

    public JetFightersGame(String jetId) {
        // TODO register real listeners on eventbus
        this.jetId = jetId;
    }

    public void create() {
        try {
            client.start();
            this.batch = new SpriteBatch();
            this.font = new BitmapFont(); // Use LibGDX's default Arial font.
            this.setScreen(new MainMenuScreen(this, jetId));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}
