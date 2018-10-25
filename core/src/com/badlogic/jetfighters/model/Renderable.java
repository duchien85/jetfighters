package com.badlogic.jetfighters.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public interface Renderable {
    float getX();

    float getY();

    Rectangle getRectangle();

    Texture getTexture();

    void moveOnNewFrame();

    boolean leftScreenBorders();
}
