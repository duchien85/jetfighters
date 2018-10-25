package com.badlogic.jetfighters.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.io.Serializable;

public class Missile implements Renderable, Serializable {
    private Jet jet;
    private float x;
    private float y;
    private transient Texture texture;
    private Rectangle rectangle;

    private Missile() {
    }

    public static Missile fromJet(Jet jet) {
        Missile missile = new Missile();
        missile.texture = new Texture(Gdx.files.internal("missile.png"));
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

    public void setRectangle(Rectangle rectangle) {this.rectangle = rectangle; }

    @Override
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

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void moveOnNewFrame() {
        this.setY(this.getY() + 600 * Gdx.graphics.getDeltaTime());
    }
}
