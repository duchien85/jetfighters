package com.badlogic.jetfighters.dto.response;

import com.badlogic.jetfighters.model.Jet;

public class NewPlayerJoinedResponse implements GameServerMessage {
    private Jet jet;

    public NewPlayerJoinedResponse(Jet jet) {
        this.jet = jet;
    }

    public Jet getJet() {
        return jet;
    }
}
