package com.badlogic.jetfighters.collision;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.jetfighters.screens.GameScreen;

import java.util.Iterator;

public class MeteorAndJetCollisionDetector implements CollisionDetector<Array<Meteor>, Array<Jet>> {

    private JetFightersGame game;
    private GameScreen gameScreen;
    private Sound explosionSound;

    public MeteorAndJetCollisionDetector(JetFightersGame game, GameScreen gameScreen, Sound explosionSound) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.explosionSound = explosionSound;
    }

    @Override
    public void collideAndRemove(Array<Meteor> meteors, Array<Jet> jets) {
        for (Iterator<Meteor> meteorIterator = gameScreen.meteors.iterator(); meteorIterator.hasNext(); ) {
            Meteor meteor = meteorIterator.next();
            for (Iterator<Jet> jetIterator = gameScreen.jets.iterator(); jetIterator.hasNext(); ) {
                Jet jet = jetIterator.next();
                if (meteor.getRectangle().overlaps(jet.getRectangle())) {
                    explosionSound.play();
                    System.out.print("Did I die? " + jet.getJetId() + " " + gameScreen.jet.getJetId());
                    if (jet.getJetId().equals(gameScreen.jet.getJetId())) {
                        game.client.reportJetDestroyed(gameScreen.jet.getJetId());
                    }
                    System.out.println("Removing jet " + jet.getJetId());
                    jetIterator.remove();
                    System.out.println("Remaining jets: " + gameScreen.jets.size);
                }
            }
        }
    }
}
