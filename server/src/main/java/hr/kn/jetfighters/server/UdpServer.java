package hr.kn.jetfighters.server;

import com.google.common.eventbus.EventBus;
import hr.kn.jetfighters.server.network.codec.GameClientMessageDecoder;
import hr.kn.jetfighters.server.network.codec.GameClientMessageHandler;
import hr.kn.jetfighters.server.network.eventbus.listener.FireMissileMessageListener;
import hr.kn.jetfighters.server.network.eventbus.listener.JetDestroyedMessageListener;
import hr.kn.jetfighters.server.network.eventbus.listener.JetMoveMessageListener;
import hr.kn.jetfighters.server.network.eventbus.listener.JoinGameMessageListener;
import hr.kn.jetfighters.server.network.timer.SpawnMeteorTimer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.AttributeKey;

import java.net.InetAddress;

public class UdpServer {

    private EventBus eventBus = new EventBus();
    private SpawnMeteorTimer spawnMeteorTimer = new SpawnMeteorTimer();

    private final Integer SERVER_PORT = 9956;
    private final Integer MAX_PACKET_SIZE = 10_000;


    public void run() throws Exception {
        eventBus.register(new JetMoveMessageListener());
        eventBus.register(new JoinGameMessageListener());
        eventBus.register(new FireMissileMessageListener());
        eventBus.register(new JetDestroyedMessageListener());


        final NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            final Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_RCVBUF, MAX_PACKET_SIZE)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(MAX_PACKET_SIZE))
                    .attr(AttributeKey.newInstance("eventBus"), eventBus)
                    .handler(channelInitializer());

            // InetAddress address = InetAddress.getLocalHost();
            InetAddress address = InetAddress.getByName("192.168.6.140");
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
                p.addLast(new GameClientMessageHandler(eventBus));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        new UdpServer().run();
    }
}
