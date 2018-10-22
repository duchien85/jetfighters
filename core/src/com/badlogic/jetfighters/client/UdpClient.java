package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.dto.request.GameClientMessage;
import com.badlogic.jetfighters.dto.request.JetMoveMessage;
import com.badlogic.jetfighters.dto.request.JoinGameMessage;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import com.google.common.eventbus.EventBus;
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
    private EventBus eventBus;

    public UdpClient(JetFightersGame game, EventBus eventBus) {
        this.game = game;
        this.eventBus = eventBus;
    }

    public void start() throws InterruptedException, UnknownHostException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioDatagramChannel.class)
//                .attr(AttributeKey.newInstance("game"), game)
//                .attr(AttributeKey.newInstance("eventBus"), eventBus)
                .handler(channelInitializer(this));

        String host = InetAddress.getLocalHost().getHostAddress();
        this.remoteAddress = new InetSocketAddress(host, 9956);
        this.channel = b.bind(0).sync().channel();
    }


    public void joinGame(String jetId) {
        send(new JoinGameMessage(jetId));
    }

    public void moveJet(String jetId, float x, float y) {
        send(new JetMoveMessage(jetId, x, y));
    }

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
                p.addLast(new ClientPacketHandler(udpClient, game));
            }
        };
    }
}
