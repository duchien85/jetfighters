package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.JetFightersCore;
import com.badlogic.jetfighters.dto.response.GameServerMessage;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
        Object object = deserialize(bytes);

        // TODO napravit decoder, pa handlere...
        if (object instanceof GameServerMessage) {
            JoinGameMessageResponse response = (JoinGameMessageResponse) object;
            if ("SUCCESS".equals(response.getStatus())) {
                game.SERVER_CONNECTED = true;
            }
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}
