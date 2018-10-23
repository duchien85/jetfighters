package hr.kn.jetfighters.server.network.eventbus.listener;

import com.badlogic.jetfighters.dto.request.JoinGameMessage;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import com.badlogic.jetfighters.dto.response.NewPlayerJoinedResponse;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.badlogic.jetfighters.model.Jet;
import com.google.common.eventbus.Subscribe;
import hr.kn.jetfighters.server.game.GameState;
import hr.kn.jetfighters.server.game.manager.ChannelAndSender;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.DatagramPacket;

public class JoinGameMessageListener implements ServerMessageListener<JoinGameMessage> {

    @Subscribe
    @Override
    public void handle(JoinGameMessage message) {
        System.out.println("Player " + message.getDesiredJetId() + " is requesting to join a game.");
        JoinGameMessageResponse response = new JoinGameMessageResponse(
                message.getDesiredJetId(), "SUCCESS", GameState.jetManager.getJets());
        ByteBuf bufResponse = Unpooled.copiedBuffer(GameMessageSerde.serialize((response)));
        ChannelFuture future = message.getCtx().channel().writeAndFlush(new DatagramPacket(bufResponse, message.getSender()));

        future.addListener(future1 -> {
            Jet newJet = new Jet(message.getDesiredJetId(), 0, 0);
            if (future.isSuccess()) {
                GameState.jetManager.getJets().put(message.getDesiredJetId(), newJet);
                broadcastNewPlayerInfo(newJet);
                GameState.channelManager.getChannels().put(
                        message.getDesiredJetId(), new ChannelAndSender(message.getCtx().channel(), message.getSender())
                );
            }
        });
    }

    private void broadcastNewPlayerInfo(Jet newJet) {
        NewPlayerJoinedResponse newPlayerMessage = new NewPlayerJoinedResponse(newJet);
        ByteBuf newPlayerBuf = Unpooled.copiedBuffer(GameMessageSerde.serialize((newPlayerMessage)));
        GameState.channelManager.getChannels().forEach((s, channelAndSender) ->
                channelAndSender.getChannel().writeAndFlush(new DatagramPacket(newPlayerBuf, channelAndSender.getSender()))
        );
    }
}
