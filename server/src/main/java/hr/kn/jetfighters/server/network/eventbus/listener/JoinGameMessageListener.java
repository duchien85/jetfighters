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

import java.util.Random;

public class JoinGameMessageListener implements ServerMessageListener<JoinGameMessage> {

    private Random r = new Random();

    @Subscribe
    @Override
    public void handle(JoinGameMessage message) {
        System.out.println("Player " + message.getDesiredJetId() + " is requesting to join a game.");

        int texture = r.nextInt(4) + 1;
        Jet newJet = new Jet(message.getDesiredJetId(), 1024 / 2 - 64 / 2, 0, texture);
        JoinGameMessageResponse response = new JoinGameMessageResponse(
                newJet, "SUCCESS", GameState.jetManager.getJets());

        ByteBuf bufResponse = Unpooled.copiedBuffer(GameMessageSerde.serialize((response)));
        ChannelFuture f1 = message.getCtx().channel().writeAndFlush(new DatagramPacket(bufResponse, message.getSender()));

        f1.addListener(f -> {
            if (f1.isSuccess()) {
                broadcastNewPlayerInfo(newJet);
                GameState.jetManager.getJets().put(newJet.getJetId(), newJet);
                GameState.channelManager.getChannels().put(
                        newJet.getJetId(), new ChannelAndSender(message.getCtx().channel(), message.getSender())
                );
            }
        });
    }

    private void broadcastNewPlayerInfo(Jet newJet) {
        NewPlayerJoinedResponse newPlayerMessage = new NewPlayerJoinedResponse(newJet);
        ByteBuf newPlayerBuf = Unpooled.copiedBuffer(GameMessageSerde.serialize((newPlayerMessage)));
        GameState.channelManager.getChannels().forEach((s, channelAndSender) -> {
                    newPlayerBuf.retain();
                    channelAndSender.getChannel().writeAndFlush(new DatagramPacket(newPlayerBuf, channelAndSender.getSender()));
                }
        );
    }
}
