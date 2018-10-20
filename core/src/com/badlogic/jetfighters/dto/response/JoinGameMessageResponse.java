package com.badlogic.jetfighters.dto.response;

public class JoinGameMessageResponse implements GameServerMessage {
    private String jetId;
    private String status;

    public JoinGameMessageResponse(String jetId, String status) {
        this.jetId = jetId;
        this.status = status;
    }

    public String getJetId() {
        return jetId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int getMessageId() {
        return 1;
    }
}
