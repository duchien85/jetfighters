package com.badlogic.jetfighters.client.eventbus;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.dto.response.FireMissileMessageResponse;
import com.badlogic.jetfighters.screens.GameScreen;
import com.google.common.eventbus.Subscribe;

public class FireMissileMessageResponseListener {

    private GameScreen gameScreen;

    public FireMissileMessageResponseListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Subscribe
    public void handle(FireMissileMessageResponse message) {
        Gdx.app.postRunnable(() -> gameScreen.missiles.add(message.getMissile()));
    }

}
