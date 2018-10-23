package com.badlogic.jetfighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.client.eventbus.JetMoveMessageResponseListener;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.model.Missile;
import com.badlogic.jetfighters.render.JetRenderer;
import com.badlogic.jetfighters.render.MeteorRenderer;
import com.badlogic.jetfighters.render.MissileRenderer;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {

    private JetFightersGame game;
    private String jetId;

    private BitmapFont font;
    private Random random = new Random();

    private SpriteBatch textBatch = new SpriteBatch();
    private SpriteBatch gameOverBatch = new SpriteBatch();
    private OrthographicCamera camera;

    private Sound explosionSound;
    private Music airplaneMusic;

    private Jet jet;
    public Array<Jet> jets;
    private Array<Missile> missiles;
    private Array<Meteor> meteors;

    private JetRenderer jetRenderer = new JetRenderer();
    private MissileRenderer missileRenderer = new MissileRenderer();
    private MeteorRenderer meteorRenderer = new MeteorRenderer();

    private Texture gameOverTexture = new Texture(Gdx.files.internal("game_over.png"));

    private long METEOR_SPWAN_TIME = 3000;
    private long lastMeteorTime = 0;

    private int numberOfHitMeteors = 0;

    private boolean GAME_OVER = false;

    public GameScreen(JetFightersGame game, String jetId) {
        JetFightersGame.eventBus.register(new JetMoveMessageResponseListener(this));
        this.game = game;
        this.jetId = jetId;
        this.font = new BitmapFont();

        // load the explosion sound effect and the airplane background "music"
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        airplaneMusic = Gdx.audio.newMusic(Gdx.files.internal("airplane.mp3"));

        // start the playback of the background music immediately
        airplaneMusic.setLooping(true);
        airplaneMusic.play();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);

        jet = new Jet(jetId, 1024 / 2 - 64 / 2, 20);

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
        //batch.setProjectionMatrix(camera.combined);
        textBatch.setProjectionMatrix(camera.combined);

        // render everything, should be in same batch?
        jetRenderer.render(jets);
        missileRenderer.render(missiles);
        meteorRenderer.render(meteors);

        this.textBatch.begin();
        this.font.draw(this.textBatch, "Destroyed meteors: " + numberOfHitMeteors, 5, 20);
        this.textBatch.end();

        // process keyboard input for jet1
        Jet newJetLocation = new Jet(jet.getJetId(), jet.getX(), jet.getY());
        if (Gdx.input.isKeyPressed(Keys.UP))
            newJetLocation.setY(newJetLocation.getY() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            newJetLocation.setY(newJetLocation.getY() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            newJetLocation.setX(newJetLocation.getX() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            newJetLocation.setX(newJetLocation.getX() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT) && jet.canShoot()) {
            missiles.add(Missile.fromJet(jet));
            jet.setLastShootTime(System.currentTimeMillis());
        }

        // make sure the jet stays within the screen bounds
        if (newJetLocation.getX() < 0) newJetLocation.setX(0);
        if (newJetLocation.getX() > 1024 - 64) newJetLocation.setX(1024 - 64);
        if (newJetLocation.getY() < 0) newJetLocation.setY(0);
        if (newJetLocation.getY() > 768 - 53) newJetLocation.setY(768 - 53);

        // move missiles, remove any that are beneath the top edge of
        // the screen or that hit the enemy. In the latter case we play back
        // a sound effect as well.
        for (Iterator<Missile> iter = missiles.iterator(); iter.hasNext(); ) {
            Missile missile = iter.next();
            missile.setY(missile.getY() + 600 * Gdx.graphics.getDeltaTime());
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
                    numberOfHitMeteors++;
                }
            }
            if (meteor.getRectangle().overlaps(jet.getRectangle())) {
                removeHitJet(jet);
            }

            if (meteor.getY() + 143 < 0) iter.remove();
        }

        if (System.currentTimeMillis() - lastMeteorTime > METEOR_SPWAN_TIME) {
            spawnNewMeteor();
            lastMeteorTime = System.currentTimeMillis();
        }


        if (GAME_OVER) {
            gameOverBatch.begin();
            gameOverBatch.draw(gameOverTexture, 1024 / 2 - 204, 768 /2 - 59);
            gameOverBatch.end();
        } else {
            game.client.moveJet(jetId, newJetLocation.getX(), newJetLocation.getY());
        }
    }

    private void removeHitJet(Jet hitJet) {
        explosionSound.play();
        for (Iterator<Jet> iter = jets.iterator(); iter.hasNext(); ) {
            Jet jet = iter.next();
            if (jet.getJetId().equals(hitJet.getJetId())) {
                iter.remove();
                GAME_OVER = true;
            }
        }
    }

    private void spawnNewMeteor() {
        meteors.add(new Meteor(random.nextInt((800) + 1), 600));
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
        textBatch.dispose();
    }
}