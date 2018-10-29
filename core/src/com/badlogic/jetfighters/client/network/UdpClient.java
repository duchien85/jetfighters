package com.badlogic.jetfighters.client.network;

import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.dto.request.*;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.badlogic.jetfighters.model.Missile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class UdpClient {

    public UdpClientState state = UdpClientState.DISCONNECTED;

    private Channel channel;
    private InetSocketAddress remoteAddress;

    private JetFightersGame game;

    public UdpClient(JetFightersGame game) {
        this.game = game;
    }

    public void start(String server) throws InterruptedException, UnknownHostException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioDatagramChannel.class)
                .handler(channelInitializer(this));

        String host = server != null ? server : InetAddress.getLocalHost().getHostAddress();
        this.remoteAddress = new InetSocketAddress(host, 9956);
        this.channel = b.bind(0).sync().channel();
    }


    public void joinGame(String jetId) {
        send(new JoinGameMessage(jetId));
    }

    public void moveJet(String jetId, float x, float y) {
        send(new JetMoveMessage(jetId, x, y));
    }

    public void fireMissile(Missile missile) { send(new FireMissileMessage(missile)); }

    public void reportJetDestroyed(String jetId) { send(new JetDestroyedMessage(jetId)); }

    private void send(GameClientMessage message) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(GameMessageSerde.serialize(message));
        if (byteBuf.readableBytes() > 0) {
            this.channel.writeAndFlush(new DatagramPacket(byteBuf, this.remoteAddress));
        }
    }

    private ChannelInitializer<NioDatagramChannel> channelInitializer(UdpClient udpClient) {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ClientPacketHandler());
            }
        };
    }
}
