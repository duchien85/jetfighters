package com.badlogic.jetfighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.dto.request.JoinGameMessage;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class MainMenuScreen implements Screen {

    private final JetFightersGame game;
    private final OrthographicCamera camera;
    private String jetId;

    public MainMenuScreen(final JetFightersGame game, final String jetId) throws UnknownHostException {
        this.game = game;
        this.jetId = jetId;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        String host = InetAddress.getLocalHost().getHostAddress();
        game.remoteAddress = new InetSocketAddress(host, 9956);
    }

    @Override
    public void show() {
        try {
            game.channel = connectToServer();
            JoinGameMessage dto = new JoinGameMessage(jetId);
            ByteBuf byteBuf = Unpooled.copiedBuffer(GameMessageSerde.serialize(dto));
            game.channel.writeAndFlush(new DatagramPacket(byteBuf, game.remoteAddress));
            // Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to JetFighters!", 100, 150);
        game.font.draw(game.batch, "Acquiring connection to server...", 100, 100);
        game.batch.end();
    }

    private Channel connectToServer() throws InterruptedException {
        Channel channel = game.client.start();
        JoinGameMessage dto = new JoinGameMessage(jetId);
        ByteBuf byteBuf = Unpooled.copiedBuffer(GameMessageSerde.serialize(dto));
        return channel.writeAndFlush(new DatagramPacket(byteBuf, game.remoteAddress)).sync().channel();
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