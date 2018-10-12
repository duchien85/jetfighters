package com.badlogic.jetfighters.dto;

import java.io.Serializable;

public class JetMoveMessage implements GameClientMessage, Serializable {
    private int messageId;
    private float x;
    private float y;

    public JetMoveMessage(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public int getMessageId() {
        return 1;
    }
}
