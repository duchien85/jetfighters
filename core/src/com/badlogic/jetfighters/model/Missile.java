package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Missile implements Renderable {
    private Jet jet;
    private float x;
    private float y;
    private final Texture texture = new Texture(Gdx.files.internal("missile.png"));
    private Rectangle rectangle;

    private Missile() {
    }

    public static Missile fromJet(Jet jet) {
        Missile missile = new Missile();
        missile.jet = jet;
        missile.x = (jet.getX() + 32) - 9;
        missile.y = jet.getY() + 89;
        missile.rectangle = new Rectangle(missile.x, missile.y, 18, 50);
        return missile;
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

    public Jet getJet() {
        return jet;
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
