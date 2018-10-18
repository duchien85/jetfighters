package com.badlogic.jetfighters.dto.response;

public class JoinGameMessageResponse implements GameServerMessage {
    private String status;

    public JoinGameMessageResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int getMessageId() {
        return 1;
    }
}
