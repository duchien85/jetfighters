package com.badlogic.jetfighters.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Meteor;

import java.util.Iterator;

public class MeteorAndJetCollisionDetector implements CollisionDetector<Array<Meteor>, Array<Jet>> {

    private Sound explosionSound;

    public MeteorAndJetCollisionDetector(Sound explosionSound) {
        this.explosionSound = explosionSound;
    }

    @Override
    public void collideAndRemove(Array<Meteor> meteors, Array<Jet> jets) {
        for (Iterator<Meteor> meteorIterator = meteors.iterator(); meteorIterator.hasNext(); ) {
            Meteor meteor = meteorIterator.next();
            for (int i = 0; i < jets.size; i++) {
                Jet jet = jets.get(i);
                if (meteor.getRectangle().overlaps(jet.getRectangle())) {
                    removeHitJet(jets, jet);
                }
            }
            if (meteor.getY() + 143 < 0) meteorIterator.remove();
        }
    }

    private void removeHitJet(Array<Jet> jets, Jet jet) {
        explosionSound.play();
        for (Iterator<Jet> jetIterator = jets.iterator(); jetIterator.hasNext(); ) {
            Jet jet1 = jetIterator.next();
            if (jet1.getJetId().equals(jet.getJetId())) {
                jetIterator.remove();
            }
        }
    }

}
