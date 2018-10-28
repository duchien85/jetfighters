package com.badlogic.jetfighters.dto.response;

import com.badlogic.jetfighters.model.Jet;

import java.util.Map;

public class JoinGameMessageResponse implements GameServerMessage {
    private Jet jet;
    private String status;
    private Map<String, Jet> jets;

    public JoinGameMessageResponse(Jet jet, String status, Map<String, Jet> jets) {
        this.jet = jet;
        this.status = status;
        this.jets = jets;
    }

    public Jet getJet() {
        return jet;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Jet> getJets() {
        return jets;
    }
}
