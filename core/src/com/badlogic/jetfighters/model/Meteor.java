package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Meteor implements Renderable {
    private float x;
    private float y;
    private final Texture texture = new Texture(Gdx.files.internal("meteor.png"));
    private final Rectangle rectangle;

    public Meteor(float x, float y) {
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, 60, 143);
    }

    public void setX(float x) {
        this.x = x;
        this.rectangle.setX(x);
    }

    public void setY(float y) {
        this.y = y;
        this.rectangle.setY(y);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }


}
