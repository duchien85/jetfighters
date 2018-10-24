package com.badlogic.jetfighters.dto.request;

import com.badlogic.jetfighters.model.Missile;

public class FireMissileMessage extends GameClientMessage {

    private Missile missile;

    public FireMissileMessage(Missile missile) {
        this.missile = missile;
    }

    public Missile getMissile() {
        return missile;
    }
}