package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Jet implements Renderable {
    private float x;
    private float y;

    private AtomicInteger currentTexture = new AtomicInteger(0);
    private final Map<Integer, Texture> textures = new HashMap<>();

    private final Rectangle rectangle;
    private long lastShootTime;

    public Jet(float x, float y) {
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, 64, 89);
        this.textures.put(0, new Texture(Gdx.files.internal("fighter/1.png")));
        this.textures.put(1, new Texture(Gdx.files.internal("fighter/2.png")));
        this.textures.put(2, new Texture(Gdx.files.internal("fighter/3.png")));
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
        if (currentTexture.get() == 3) {
            currentTexture.set(0);
        }
        return textures.get(currentTexture.getAndAdd(1));
    }


}
