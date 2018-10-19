package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.JetFightersCore;
import com.badlogic.jetfighters.dto.response.GameServerMessage;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class ClientPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private JetFightersCore game;

    public ClientPacketHandler(JetFightersCore game) {
        this.game = game;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf buf = msg.content();
        int readable = buf.readableBytes();
        byte[] bytes = new byte[readable];
        buf.readBytes(bytes);
        System.out.println("Received packet, length: " + bytes.length);
        Object object = GameMessageSerde.deserialize(bytes);

        // TODO napravit decoder, pa handlere...
        if (object instanceof GameServerMessage) {
            JoinGameMessageResponse response = (JoinGameMessageResponse) object;
            if ("SUCCESS".equals(response.getStatus())) {
                game.SERVER_CONNECTED = true;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}
