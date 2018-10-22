package com.badlogic.jetfighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.jetfighters.JetFightersGame;

public class MainMenuScreen implements Screen {

    private final JetFightersGame game;
    private final OrthographicCamera camera;

    private String jetId;
    private Texture pilotTexture = new Texture(Gdx.files.internal("jet_pilot.png"));

    private int attemptNumber = 1;
    private long lastConnectionAttemptTimestamp = System.currentTimeMillis();

    public MainMenuScreen(final JetFightersGame game, final String jetId) {
        this.game = game;
        this.jetId = jetId;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1024, 768);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(pilotTexture, 162, 100);
        game.font.draw(game.batch, "Welcome to JetFighters!", 100, 150);
        game.font.draw(game.batch, "Acquiring connection to server... [attempt " + attemptNumber + "]", 100, 100);
        game.batch.end();
        tryToReconnectToServer();
    }

    private void tryToReconnectToServer() {
        if (System.currentTimeMillis() - lastConnectionAttemptTimestamp > 5000) {
            game.client.joinGame(jetId);
            this.attemptNumber++;
            this.lastConnectionAttemptTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}