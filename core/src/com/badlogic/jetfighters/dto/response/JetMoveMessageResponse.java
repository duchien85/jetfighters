package com.badlogic.jetfighters.dto.response;

public class JetMoveMessageResponse implements GameServerMessage {
    private String jetId;
    private float x;
    private float y;

    public JetMoveMessageResponse(String jetId, float x, float y) {
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
}
