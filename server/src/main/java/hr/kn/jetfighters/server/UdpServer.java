package hr.kn.jetfighters.server;

import hr.kn.jetfighters.server.codec.GameClientMessageDecoder;
import hr.kn.jetfighters.server.codec.GameClientMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetAddress;

public class UdpServer {

    private final Integer SERVER_PORT = 9956;
    private final Integer MAX_PACKET_SIZE = 10_000;


    public void run() throws Exception {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            final Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_RCVBUF, MAX_PACKET_SIZE)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(MAX_PACKET_SIZE))
                    .handler(channelInitializer());

            InetAddress address = InetAddress.getLocalHost();
            System.out.printf("Waiting for messages [%s:%d]\n", String.format(address.toString()), SERVER_PORT);
            b.bind(address, SERVER_PORT).sync().channel().closeFuture().await();
        } finally {
        }
    }

    private ChannelInitializer<NioDatagramChannel> channelInitializer() {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new GameClientMessageDecoder());
                p.addLast(new GameClientMessageHandler());
            }
        };
    }

    public static void main(String[] args) throws Exception {
        new UdpServer().run();
    }
}
