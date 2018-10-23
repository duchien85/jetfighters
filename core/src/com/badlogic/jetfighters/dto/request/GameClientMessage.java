package com.badlogic.jetfighters.dto.request;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class GameClientMessage implements Serializable {
    private InetSocketAddress sender;
    private ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public InetSocketAddress getSender() {
        return sender;
    }

    public void setSender(InetSocketAddress sender) {
        this.sender = sender;
    }
}
