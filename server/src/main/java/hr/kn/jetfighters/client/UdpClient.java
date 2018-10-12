package hr.kn.jetfighters.client;

import hr.kn.jetfighters.JetFighterDto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class UdpClient {

    private static Channel channel;

    private static String host;
    private static int port = 9956;

    public static void main(String[] args) throws UnknownHostException {
        host = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        UdpClient client = new UdpClient();

        try {
            channel = client.start();
            JetFighterDto dto = new JetFighterDto(22.55f, 17.34f);
            ByteBuf byteBuf = Unpooled.copiedBuffer(serialize(dto));
            ChannelFuture channelFuture = channel.writeAndFlush(new DatagramPacket(byteBuf, remoteAddress)).sync();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("Writing to channel success!");
                }
            });
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }


    public Channel start() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioDatagramChannel.class)
                .handler(channelInitializer());
        return channel = b.bind(0).sync().channel();
    }

    private ChannelInitializer<NioDatagramChannel> channelInitializer() {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ClientPacketHandler());
            }
        };
    }
}
