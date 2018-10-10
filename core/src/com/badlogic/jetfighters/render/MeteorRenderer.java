package com.badlogic.jetfighters.render;

import com.badlogic.jetfighters.model.Meteor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class MeteorRenderer implements Renderer<Meteor> {
    @Override
    public void render(Array<Meteor> meteors) {
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        for (Iterator<Meteor> iter = meteors.iterator(); iter.hasNext(); ) {
            Meteor meteor = iter.next();
            batch.draw(meteor.getTexture(), meteor.getX(), meteor.getY());
        }
        batch.end();
    }
}
