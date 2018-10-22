package hr.kn.jetfighters.server.network.eventbus.listener;

import com.badlogic.jetfighters.dto.request.JetMoveMessage;
import com.badlogic.jetfighters.dto.response.JetMoveMessageResponse;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.google.common.eventbus.Subscribe;
import hr.kn.jetfighters.server.game.GameState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

public class JetMoveMessageListener implements ServerMessageListener<JetMoveMessage> {

    @Subscribe
    @Override
    public void handle(JetMoveMessage message) {
        GameState.jetManager.updateJetLocation(message.getJetId(), message.getX(), message.getY());

        JetMoveMessageResponse response = new JetMoveMessageResponse(message.getJetId(), message.getX(), message.getY());
        ByteBuf bufResponse = Unpooled.copiedBuffer(GameMessageSerde.serialize((response)));
        GameState.channelManager.getChannels().forEach((s, channel) ->
                channel.writeAndFlush(new DatagramPacket(bufResponse, message.getSender())));
    }

}
