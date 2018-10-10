package com.badlogic.jetfighters;

import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.model.Missile;
import com.badlogic.jetfighters.render.JetRenderer;
import com.badlogic.jetfighters.render.MeteorRenderer;
import com.badlogic.jetfighters.render.MissileRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

public class JetFightersCore extends ApplicationAdapter {

    private Random random = new Random();

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Sound dropSound;
    private Music rainMusic;

    private Jet jet;
    private Array<Jet> spaceships;
    private Array<Missile> missiles;
    private Array<Meteor> meteors;

    private JetRenderer jetRenderer = new JetRenderer();
    private MissileRenderer missileRenderer = new MissileRenderer();
    private MeteorRenderer meteorRenderer = new MeteorRenderer();

    private long METEOR_SPWAN_TIME = 3000;
    private long lastMeteorTime = 0;

    @Override
    public void create() {
        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        jet = new Jet(800 / 2 - 64 / 2, 20);

        // create containers and spawn the first jet
        missiles = new Array<Missile>();
        spaceships = new Array<Jet>();
        meteors = new Array<Meteor>();
        spaceships.add(jet);
    }

    @Override
    public void render() {
        // Clear screen and fill with dark blue
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // render everything, should be in same batch?
        jetRenderer.render(spaceships);
        missileRenderer.render(missiles);
        meteorRenderer.render(meteors);

        // process keyboard input
        if (Gdx.input.isKeyPressed(Keys.UP)) jet.setY(jet.getY() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.DOWN)) jet.setY(jet.getY() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.LEFT)) jet.setX(jet.getX() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) jet.setX(jet.getX() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.SPACE) && jet.canShoot()) {
            missiles.add(Missile.fromJet(jet));
            jet.setLastShootTime(System.currentTimeMillis());
        }

        // make sure the bucket stays within the screen bounds
        if (jet.getX() < 0) jet.setX(0);
        if (jet.getX() > 800 - 64) jet.setX(800 - 64);


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
                    dropSound.play();
                    iter.remove();
                    iterMissile.remove();
                }
            }
            if (meteor.getRectangle().overlaps(jet.getRectangle())) {
                dropSound.play();
                System.exit(0);
                //iter.remove();
            }

            if (meteor.getY() + 143 < 0) iter.remove();
        }

        if (System.currentTimeMillis() - lastMeteorTime > METEOR_SPWAN_TIME) {
            spawnNewMeteor();
            lastMeteorTime = System.currentTimeMillis();
        }
    }

    private void spawnNewMeteor() {
        meteors.add(new Meteor(random.nextInt((800) + 1), 600));
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
}