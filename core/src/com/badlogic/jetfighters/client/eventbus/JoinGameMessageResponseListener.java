package com.badlogic.jetfighters.client.eventbus;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.client.network.UdpClientState;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import com.badlogic.jetfighters.screens.GameScreen;
import com.google.common.eventbus.Subscribe;

public class JoinGameMessageResponseListener {

    private JetFightersGame game;

    public JoinGameMessageResponseListener(JetFightersGame game) {
        this.game = game;
    }

    @Subscribe
    public void handle(JoinGameMessageResponse message) {
        if ("SUCCESS".equals(message.getStatus())) {
            game.client.state = UdpClientState.CONNECTED;
            Gdx.app.postRunnable(() -> {
                game.setScreen(new GameScreen(game, message.getJetId()));
                System.out.println("Successful server connection");
            });
        }

    }

}
