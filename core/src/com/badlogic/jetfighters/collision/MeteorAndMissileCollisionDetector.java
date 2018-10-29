package com.badlogic.jetfighters.collision;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.model.Missile;
import com.badlogic.jetfighters.screens.GameScreen;

import java.util.Iterator;

public class MeteorAndMissileCollisionDetector implements CollisionDetector<Array<Meteor>, Array<Missile>> {

    private GameScreen gameScreen;
    private Sound explosionSound;

    public MeteorAndMissileCollisionDetector(GameScreen gameScreen, Sound explosionSound) {
        this.gameScreen = gameScreen;
        this.explosionSound = explosionSound;
    }

    @Override
    public void collideAndRemove(Array<Meteor> meteors, Array<Missile> missiles) {
        for (Iterator<Meteor> meteorIterator = meteors.iterator(); meteorIterator.hasNext(); ) {
            Meteor meteor = meteorIterator.next();
            for (Iterator<Missile> missileIterator = missiles.iterator(); missileIterator.hasNext(); ) {
                Missile missile = missileIterator.next();
                if (meteor.getRectangle().overlaps(missile.getRectangle())) {
                    explosionSound.play();
                    meteorIterator.remove();
                    missileIterator.remove();
                    if (missile.getJet().getJetId().equals(gameScreen.jet.getJetId())) {
                        // TODO notify server about hit
                        gameScreen.numberOfDestroyedMeteors++;
                    }
                }

                if (missile.getY() + 32 > 768) missileIterator.remove();
            }
            if (meteor.getY() + 143 < 0) meteorIterator.remove();
        }
    }

}
