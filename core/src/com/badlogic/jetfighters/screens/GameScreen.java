package com.badlogic.jetfighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.dto.request.JetMoveMessage;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.model.Missile;
import com.badlogic.jetfighters.render.JetRenderer;
import com.badlogic.jetfighters.render.MeteorRenderer;
import com.badlogic.jetfighters.render.MissileRenderer;
import com.google.common.eventbus.EventBus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {

    private EventBus eventBus = new EventBus();

    private JetFightersGame game;
    private String jetId;

    private Random random = new Random();

    private InetSocketAddress remoteAddress;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Sound explosionSound;
    private Music airplaneMusic;

    private Jet jet;
    private Array<Jet> jets;
    private Array<Missile> missiles;
    private Array<Meteor> meteors;

    private JetRenderer jetRenderer = new JetRenderer();
    private MissileRenderer missileRenderer = new MissileRenderer();
    private MeteorRenderer meteorRenderer = new MeteorRenderer();

    private long METEOR_SPWAN_TIME = 3000;
    private long lastMeteorTime = 0;

    public GameScreen(JetFightersGame game, String jetId) {
        this.game = game;
        this.jetId = jetId;

        // load the explosion sound effect and the airplane background "music"
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        airplaneMusic = Gdx.audio.newMusic(Gdx.files.internal("airplane.mp3"));

        // start the playback of the background music immediately
        airplaneMusic.setLooping(true);
        airplaneMusic.play();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        jet = new Jet(jetId, 800 / 2 - 64 / 2, 20);

        // create containers and spawn the first jet
        missiles = new Array<>();
        jets = new Array<>();
        meteors = new Array<>();
        jets.add(jet);
    }

    @Override
    public void render(float delta) {
        // Clear screen and fill with dark blue
        Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // render everything, should be in same batch?
        jetRenderer.render(jets);
        missileRenderer.render(missiles);
        meteorRenderer.render(meteors);

        // process keyboard input for jet1
        if (Gdx.input.isKeyPressed(Keys.UP)) jet.setY(jet.getY() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.DOWN)) jet.setY(jet.getY() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.LEFT)) jet.setX(jet.getX() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) jet.setX(jet.getX() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT) && jet.canShoot()) {
            missiles.add(Missile.fromJet(jet));
            jet.setLastShootTime(System.currentTimeMillis());
        }

        // make sure the jet stays within the screen bounds
        if (jet.getX() < 0) jet.setX(0);
        if (jet.getX() > 800 - 64) jet.setX(800 - 64);
        if (jet.getY() < 0) jet.setY(0);
        if (jet.getY() > 480 - 53) jet.setY(480 - 53);

        // move missiles, remove any that are beneath the top edge of
        // the screen or that hit the enemy. In the latter case we play back
        // a sound effect as well.
        for (Iterator<Missile> iter = missiles.iterator(); iter.hasNext(); ) {
            Missile missile = iter.next();
            missile.setY(missile.getY() + 200 * Gdx.graphics.getDeltaTime());
            if (missile.getY() + 32 > 800) iter.remove();
        }

        for (Iterator<Meteor> iter = meteors.iterator(); iter.hasNext(); ) {
            Meteor meteor = iter.next();
            meteor.setY(meteor.getY() - 200 * Gdx.graphics.getDeltaTime());

            for (Iterator<Missile> iterMissile = missiles.iterator(); iterMissile.hasNext(); ) {
                Missile missile = iterMissile.next();
                if (meteor.getRectangle().overlaps(missile.getRectangle())) {
                    explosionSound.play();
                    iter.remove();
                    iterMissile.remove();
                }
            }
            if (meteor.getRectangle().overlaps(jet.getRectangle())) {
                explosionSound.play();
                jets.iterator().next();
                jets.iterator().remove();
            }

            if (meteor.getY() + 143 < 0) iter.remove();
        }

        if (System.currentTimeMillis() - lastMeteorTime > METEOR_SPWAN_TIME) {
            spawnNewMeteor();
            lastMeteorTime = System.currentTimeMillis();
        }
        reportNewCoordinatesToServer();
    }

    private void spawnNewMeteor() {
        meteors.add(new Meteor(random.nextInt((800) + 1), 600));
    }

    private void reportNewCoordinatesToServer() {
        JetMoveMessage dto = new JetMoveMessage(jetId, jet.getX(), jet.getY());
        ByteBuf byteBuf = Unpooled.copiedBuffer(GameMessageSerde.serialize(dto));
        if (byteBuf.readableBytes() > 0) {
            game.channel.writeAndFlush(new DatagramPacket(byteBuf, remoteAddress));
        }
    }

    @Override
    public void show() {

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
        explosionSound.dispose();
        airplaneMusic.dispose();
        batch.dispose();
    }
}