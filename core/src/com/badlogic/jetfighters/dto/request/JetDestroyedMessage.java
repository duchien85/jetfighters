package com.badlogic.jetfighters.dto.request;

public class JetDestroyedMessage extends GameClientMessage {

    private String jetId;

    public JetDestroyedMessage(String jetId) {
        this.jetId = jetId;
    }

    public String getJetId() {
        return jetId;
    }
}
