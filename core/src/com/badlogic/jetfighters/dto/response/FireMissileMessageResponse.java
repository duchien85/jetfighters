package com.badlogic.jetfighters.dto.response;

import com.badlogic.jetfighters.dto.request.FireMissileMessage;

public class FireMissileMessageResponse extends FireMissileMessage {
    public FireMissileMessageResponse(FireMissileMessage message) {
        super(message.getMissile());
    }
}