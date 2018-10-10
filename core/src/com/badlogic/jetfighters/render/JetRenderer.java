package com.badlogic.jetfighters.render;

import com.badlogic.jetfighters.model.Jet;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class JetRenderer implements Renderer<Jet> {
    @Override
    public void render(Array<Jet> spaceships) {
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        for (Iterator<Jet> iter = spaceships.iterator(); iter.hasNext(); ) {
            Jet jet = iter.next();
            batch.draw(jet.getTexture(), jet.getX(), jet.getY());
        }
        batch.end();
    }
}
