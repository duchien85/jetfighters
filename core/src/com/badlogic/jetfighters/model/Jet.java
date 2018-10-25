package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Jet implements Renderable, Serializable {
    private String jetId;
    private float x;
    private float y;

    private AtomicInteger currentTexture = new AtomicInteger(0);
    private transient Map<Integer, Texture> textures;

    private final Rectangle rectangle;
    private long lastShootTime;

    public Jet(String jetId, float x, float y) {
        this.jetId = jetId;
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, 64, 53);

        try {
            initTextures();
        } catch (Exception e) {
            // TODO stupid Exception ignore for server which doesn't have textures
        }
    }

    public void initTextures() {
        this.textures = new HashMap<>();
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

    @Override
    public Texture getTexture() {
        if (textures.size() == 0) {
            initTextures();
        }
        if (currentTexture.get() == 3) {
            currentTexture.set(0);
        }
        return textures.get(currentTexture.getAndAdd(1));
    }

    @Override
    public void moveOnNewFrame() {
        // jet doesn't move on it's own
    }

}
