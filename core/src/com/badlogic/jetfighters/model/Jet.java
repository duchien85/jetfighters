package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.io.Serializable;

public class Jet implements Renderable, Serializable {
    private String jetId;
    private float x;
    private float y;

    private int textureNumber;
    private transient Texture texture;

    private final Rectangle rectangle;
    private long lastShootTime;

    public Jet(String jetId, float x, float y, int textureNumber) {
        this.jetId = jetId;
        this.x = x;
        this.y = y;
        this.textureNumber = textureNumber;
        this.rectangle = new Rectangle(x, y, 64, 53);

        try {
            this.texture = new Texture(Gdx.files.internal("fighter/" + textureNumber + ".png"));
        } catch (Exception e) {
            // TODO stupid Exception ignore for server which doesn't have textures
        }
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

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getJetId() {
        return jetId;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public int getTextureNumber() {
        return textureNumber;
    }

    @Override
    public Texture getTexture() {
        return texture != null ? texture : new Texture(Gdx.files.internal("fighter/" + textureNumber + ".png"));
    }

    @Override
    public void moveOnNewFrame() {
        // jet doesn't move on it's own
    }

    @Override
    public boolean leftScreenBorders() {
        return false;
    }

    @Override
    public String toString() {
        return "Jet{" +
                "jetId='" + jetId + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", textureNumber=" + textureNumber +
                ", texture=" + texture +
                ", rectangle=" + rectangle +
                ", lastShootTime=" + lastShootTime +
                '}';
    }
}
