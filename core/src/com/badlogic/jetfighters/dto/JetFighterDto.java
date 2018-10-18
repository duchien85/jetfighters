package com.badlogic.jetfighters.dto;

import java.io.Serializable;

@Deprecated
public class JetFighterDto implements Serializable {
    private float x;
    private float y;

    public JetFighterDto(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
