package com.badlogic.jetfighters.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.dto.response.GameServerMessage;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.badlogic.jetfighters.screens.GameScreen;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class ClientPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final JetFightersGame game;

    public ClientPacketHandler(JetFightersGame game) {
        this.game = game;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        // JetFightersGame game = (JetFightersGame) ctx.channel().attr(AttributeKey.newInstance("game")).get();
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
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new GameScreen(game, response.getJetId()));
                    System.out.print("Succ server connect with user");
                });
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}
