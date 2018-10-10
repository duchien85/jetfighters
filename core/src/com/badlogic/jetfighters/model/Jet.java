package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Jet implements Renderable {
    private float x;
    private float y;
    private final Texture texture = new Texture(Gdx.files.internal("raptor.png"));
    private final Rectangle rectangle;
    private long lastShootTime;

    public Jet(float x, float y) {
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, 64, 89);
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShootTime > 300;
    }

    public void setLastShootTime(long lastShootTime) {
        this.lastShootTime = lastShootTime;
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
