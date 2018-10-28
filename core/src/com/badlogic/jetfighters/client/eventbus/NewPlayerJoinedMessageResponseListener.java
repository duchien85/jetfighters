package com.badlogic.jetfighters.client.eventbus;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.dto.response.NewPlayerJoinedResponse;
import com.badlogic.jetfighters.screens.GameScreen;
import com.google.common.eventbus.Subscribe;

public class NewPlayerJoinedMessageResponseListener {

    private GameScreen gameScreen;

    public NewPlayerJoinedMessageResponseListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Subscribe
    public void handle(NewPlayerJoinedResponse message) {
        System.out.println("New player joined: " + message.getJet());
        Gdx.app.postRunnable(() ->
            this.gameScreen.jets.add(message.getJet())
        );
    }

}
