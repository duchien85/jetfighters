package com.badlogic.jetfighters.dto.response;

import com.badlogic.jetfighters.model.Jet;

import java.util.Map;

public class JoinGameMessageResponse implements GameServerMessage {
    private String jetId;
    private String status;
    private Map<String, Jet> jets;

    public JoinGameMessageResponse(String jetId, String status, Map<String, Jet> jets) {
        this.jetId = jetId;
        this.status = status;
        this.jets = jets;
    }

    public String getJetId() {
        return jetId;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Jet> getJets() {
        return jets;
    }
}
