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
import com.badlogic.jetfighters.client.eventbus.FireMissileMessageResponseListener;
import com.badlogic.jetfighters.client.eventbus.JetMoveMessageResponseListener;
import com.badlogic.jetfighters.client.eventbus.NewPlayerJoinedMessageResponseListener;
import com.badlogic.jetfighters.client.eventbus.SpawnNewMeteorListener;
import com.badlogic.jetfighters.collision.MeteorAndJetCollisionDetector;
import com.badlogic.jetfighters.collision.MeteorAndMissileCollisionDetector;
import com.badlogic.jetfighters.controls.JetMovementControls;
import com.badlogic.jetfighters.controls.KeyPressController;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.model.Missile;
import com.badlogic.jetfighters.render.JetRenderer;
import com.badlogic.jetfighters.render.MeteorRenderer;
import com.badlogic.jetfighters.render.MissileRenderer;

import java.util.Iterator;

public class GameScreen implements Screen {

    private JetFightersGame game;
    private String jetId;

    private BitmapFont font = new BitmapFont();
    private Texture playerBarTexture = new Texture(Gdx.files.internal("player_bar.png"));

    private SpriteBatch textBatch = new SpriteBatch();
    private SpriteBatch gameOverBatch = new SpriteBatch();
    private OrthographicCamera camera = new OrthographicCamera();

    private Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
    private Music airplaneMusic = Gdx.audio.newMusic(Gdx.files.internal("airplane.mp3"));

    public Jet jet;
    public Array<Jet> jets;
    public Array<Missile> missiles;
    public Array<Meteor> meteors;

    /* Operators and handlers */
    private KeyPressController<Jet> keyPressControllerJet;
    private JetRenderer jetRenderer = new JetRenderer();
    private MissileRenderer missileRenderer = new MissileRenderer();
    private MeteorRenderer meteorRenderer = new MeteorRenderer();
    private MeteorAndJetCollisionDetector meteorAndJetCollisionDetector;
    private MeteorAndMissileCollisionDetector meteorAndMissileCollisionDetector;


    private Texture gameOverTexture = new Texture(Gdx.files.internal("game_over.png"));

    public int numberOfDestroyedMeteors = 0;
    private boolean GAME_OVER = false;

    public GameScreen(JetFightersGame game, Jet jet) {
        JetFightersGame.eventBus.register(new JetMoveMessageResponseListener(this));
        JetFightersGame.eventBus.register(new NewPlayerJoinedMessageResponseListener(this));
        JetFightersGame.eventBus.register(new SpawnNewMeteorListener(this));
        JetFightersGame.eventBus.register(new FireMissileMessageResponseListener(this));

        this.game = game;
        this.jetId = jet.getJetId();

        this.meteorAndJetCollisionDetector = new MeteorAndJetCollisionDetector(game, this, explosionSound);
        this.meteorAndMissileCollisionDetector = new MeteorAndMissileCollisionDetector(this, explosionSound);
        this.keyPressControllerJet = new JetMovementControls(game);

        // start the playback of the background music immediately
        this.airplaneMusic.setLooping(true);
        this.airplaneMusic.play();

        // create the camera and the SpriteBatch
        this.camera.setToOrtho(false, 1024, 768);

        // create containers and spawn the first jet
        this.jet = jet;
        this.jets = Array.with(jet);
        this.missiles = new Array<>();
        this.meteors = new Array<>();

        this.font.setColor(0, 0, 0, 100);
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
        int xPlayer = 0;
        int xFont = 45;
        for (Jet player : jets) {
            this.textBatch.draw(playerBarTexture, xPlayer, 0);
            this.font.draw(this.textBatch, player.getJetId() + "\nMeteors: " + numberOfDestroyedMeteors, xFont, 40);
            xFont += 205;
            xPlayer += 205;
        }
        this.textBatch.end();

        // process start new game
        if (GAME_OVER && Gdx.input.isKeyPressed(Keys.ANY_KEY)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // process keyboard input and simulate movement that will be sent to server
        Jet newJetLocation = keyPressControllerJet.processControls(Gdx.input, jet);

        // make sure the jet stays within the screen bounds
        if (newJetLocation.getX() < 0) newJetLocation.setX(0);
        if (newJetLocation.getX() > 1024 - 64) newJetLocation.setX(1024 - 64);
        if (newJetLocation.getY() < 0) newJetLocation.setY(0);
        if (newJetLocation.getY() > 768 - 53) newJetLocation.setY(768 - 53);


        // Move meteors and missiles as they move automatically without input
        for (Iterator<Meteor> meteorIterator = meteors.iterator(); meteorIterator.hasNext(); ) {
            Meteor meteor = meteorIterator.next();
            meteor.moveOnNewFrame();
            if (meteor.leftScreenBorders()) {
                meteorIterator.remove();
            }
        }

        for (Iterator<Missile> missileIterator = missiles.iterator(); missileIterator.hasNext(); ) {
            Missile missile = missileIterator.next();
            missile.moveOnNewFrame();
            if (missile.leftScreenBorders()) {
                missileIterator.remove();
            }
        }

        // Detect collisions and remove destroyed object
        meteorAndMissileCollisionDetector.collideAndRemove(meteors, missiles);
        meteorAndJetCollisionDetector.collideAndRemove(meteors, jets);

        if (jets.size == 0) {
            GAME_OVER = true;
        }

        if (GAME_OVER) {
            gameOverBatch.begin();
            gameOverBatch.draw(gameOverTexture, 1024 / 2 - 204, 768 / 2 - 59);
            gameOverBatch.end();
        } else {
            game.client.moveJet(jetId, newJetLocation.getX(), newJetLocation.getY());
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
        textBatch.dispose();
    }
}