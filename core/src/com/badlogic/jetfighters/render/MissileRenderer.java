package com.badlogic.jetfighters.render;

import com.badlogic.jetfighters.model.Missile;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class MissileRenderer implements Renderer<Missile> {
    @Override
    public void render(Array<Missile> missiles) {
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        for (Iterator<Missile> iter = missiles.iterator(); iter.hasNext(); ) {
            Missile missile = iter.next();
            batch.draw(missile.getTexture(), missile.getX(), missile.getY());
        }
        batch.end();
    }
}
