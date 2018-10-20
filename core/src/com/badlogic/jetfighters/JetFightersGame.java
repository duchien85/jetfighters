package com.badlogic.jetfighters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.jetfighters.client.UdpClient;
import com.badlogic.jetfighters.screens.MainMenuScreen;
import com.google.common.eventbus.EventBus;
import io.netty.channel.Channel;

import java.net.UnknownHostException;

public class JetFightersGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public Channel channel;
    public EventBus eventBus = new EventBus();
    public UdpClient client = new UdpClient(this, eventBus);

    private String jetId;

    public JetFightersGame(String jetId) {
        // TODO register real listeners on eventbus
        eventBus.register("");
        this.jetId = jetId;
    }

    public void create() {
        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
        font = new BitmapFont();
        try {
            this.setScreen(new MainMenuScreen(this, jetId));
        } catch (UnknownHostException e) {
            e.printStackTrace();
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