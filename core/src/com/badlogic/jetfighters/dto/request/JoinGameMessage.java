package com.badlogic.jetfighters.dto.request;

public class JoinGameMessage extends GameClientMessage {

    private String desiredJetId;

    public JoinGameMessage(String desiredJetId) {
        this.desiredJetId = desiredJetId;
    }

    public String getDesiredJetId() {
        return desiredJetId;
    }

    @Override
    public int getMessageId() {
        return 2;
    }
}
