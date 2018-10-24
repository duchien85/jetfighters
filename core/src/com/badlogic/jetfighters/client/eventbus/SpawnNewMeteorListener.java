package com.badlogic.jetfighters.client.eventbus;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.dto.response.SpawnNewMeteor;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.screens.GameScreen;
import com.google.common.eventbus.Subscribe;

public class SpawnNewMeteorListener {

    private GameScreen gameScreen;

    public SpawnNewMeteorListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Subscribe
    public void handle(SpawnNewMeteor message) {
        Gdx.app.postRunnable(() -> {
            gameScreen.meteors.add(new Meteor(message.getX(), message.getY()));
        });
    }

}
