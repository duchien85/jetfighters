package hr.kn.jetfighters.server.network.eventbus.listener;

import com.badlogic.jetfighters.dto.request.FireMissileMessage;
import com.badlogic.jetfighters.dto.request.JetMoveMessage;
import com.badlogic.jetfighters.dto.response.FireMissileMessageResponse;
import com.badlogic.jetfighters.dto.response.JetMoveMessageResponse;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.google.common.eventbus.Subscribe;
import hr.kn.jetfighters.server.game.GameState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

public class FireMissileMessageListener implements ServerMessageListener<FireMissileMessage> {

    @Subscribe
    @Override
    public void handle(FireMissileMessage message) {
        FireMissileMessageResponse response = new FireMissileMessageResponse(message);
        ByteBuf bufResponse = Unpooled.copiedBuffer(GameMessageSerde.serialize((response)));
        GameState.channelManager.getChannels().forEach((s, channelAndSender) -> {
            bufResponse.retain();
            channelAndSender.getChannel().writeAndFlush(new DatagramPacket(bufResponse, channelAndSender.getSender()));
        });

    }

}
