package com.badlogic.jetfighters.client.eventbus;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.client.network.UdpClientState;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.screens.GameScreen;
import com.google.common.eventbus.Subscribe;

import java.util.Map;

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
                GameScreen gameScreen = new GameScreen(game, message.getJetId());
                for (Map.Entry<String, Jet> jetEntry : message.getJets().entrySet()) {
                    jetEntry.getValue().initTextures();
                    gameScreen.jets.add(jetEntry.getValue());
                }
                game.setScreen(gameScreen);
                System.out.println("Successful server connection");
            });
        }

    }

}
