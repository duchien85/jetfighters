package com.badlogic.jetfighters.client.eventbus;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.dto.response.JetMoveMessageResponse;
import com.badlogic.jetfighters.screens.GameScreen;
import com.google.common.eventbus.Subscribe;

public class JetMoveMessageResponseListener {

    private GameScreen gameScreen;

    public JetMoveMessageResponseListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Subscribe
    public void handle(JetMoveMessageResponse message) {
        Gdx.app.postRunnable(() -> gameScreen.jets.forEach(jet -> {
            if (jet.getJetId().equals(message.getJetId())) {
                jet.setX(message.getX());
                jet.setY(message.getY());
            }
        }));
    }

}
