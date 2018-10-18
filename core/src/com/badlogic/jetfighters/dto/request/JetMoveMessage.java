package com.badlogic.jetfighters.dto.request;

public class JetMoveMessage extends GameClientMessage {
    private String jetId;
    private float x;
    private float y;

    public JetMoveMessage(String jetId, float x, float y) {
        this.jetId = jetId;
        this.x = x;
        this.y = y;
    }

    public String getJetId() {
        return jetId;
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
