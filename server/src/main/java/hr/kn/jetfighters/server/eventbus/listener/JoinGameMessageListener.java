package hr.kn.jetfighters.server.eventbus.listener;

import com.badlogic.jetfighters.dto.request.JoinGameMessage;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.google.common.eventbus.Subscribe;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

public class JoinGameMessageListener implements ServerMessageListener<JoinGameMessage> {

    @Subscribe
    @Override
    public void handle(JoinGameMessage message) {
        System.out.println("Player " + message.getDesiredJetId() + " is requesting to join a game.");
        JoinGameMessageResponse response = new JoinGameMessageResponse(message.getDesiredJetId(), "SUCCESS");
        ByteBuf bufResponse = Unpooled.copiedBuffer(GameMessageSerde.serialize((response)));
        message.getCtx().channel().writeAndFlush(new DatagramPacket(bufResponse, message.getSender()));
    }
}
