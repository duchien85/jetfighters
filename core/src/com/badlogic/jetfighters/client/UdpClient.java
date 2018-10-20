package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.JetFightersGame;
import com.google.common.eventbus.EventBus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.AttributeKey;

public class UdpClient {

    private JetFightersGame game;
    private EventBus eventBus;

    public UdpClient(JetFightersGame game, EventBus eventBus) {
        this.game = game;
        this.eventBus = eventBus;
    }

    public Channel start() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioDatagramChannel.class)
//                .attr(AttributeKey.newInstance("game"), game)
//                .attr(AttributeKey.newInstance("eventBus"), eventBus)
                .handler(channelInitializer());
        return b.bind(0).sync().channel();
    }

    private ChannelInitializer<NioDatagramChannel> channelInitializer() {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ClientPacketHandler(game));
            }
        };
    }
}
