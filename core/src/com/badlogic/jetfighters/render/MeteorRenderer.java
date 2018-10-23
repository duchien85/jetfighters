package com.badlogic.jetfighters.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.model.Meteor;

import java.util.Iterator;

public class MeteorRenderer implements Renderer<Meteor> {

    private SpriteBatch batch = new SpriteBatch();

    @Override
    public void render(Array<Meteor> meteors) {
        batch.begin();
        for (Iterator<Meteor> iter = meteors.iterator(); iter.hasNext(); ) {
            Meteor meteor = iter.next();
            batch.draw(meteor.getTexture(), meteor.getX(), meteor.getY());
        }
        batch.end();
    }
}
