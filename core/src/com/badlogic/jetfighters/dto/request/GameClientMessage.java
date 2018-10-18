package com.badlogic.jetfighters.dto.request;

import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class GameClientMessage implements Serializable {
    private InetSocketAddress sender;

    abstract int getMessageId();

    public InetSocketAddress getSender() {
        return sender;
    }

    public void setSender(InetSocketAddress sender) {
        this.sender = sender;
    }
}
