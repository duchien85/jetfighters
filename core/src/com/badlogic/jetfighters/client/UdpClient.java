package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.JetFightersCore;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpClient {

    public Channel start(JetFightersCore game) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioDatagramChannel.class)
                .handler(channelInitializer(game));
        return b.bind(0).sync().channel();
    }

    private ChannelInitializer<NioDatagramChannel> channelInitializer(JetFightersCore game) {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ClientPacketHandler(game));
            }
        };
    }
}
