package com.badlogic.jetfighters.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.jetfighters.model.Missile;

import java.util.Iterator;

public class MissileRenderer implements Renderer<Missile> {

    private SpriteBatch batch = new SpriteBatch();

    @Override
    public void render(Array<Missile> missiles) {
        batch.begin();
        for (Iterator<Missile> iter = missiles.iterator(); iter.hasNext(); ) {
            Missile missile = iter.next();
            batch.draw(missile.getTexture(), missile.getX(), missile.getY());
        }
        batch.end();
    }
}
