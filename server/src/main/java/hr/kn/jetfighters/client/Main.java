package hr.kn.jetfighters.client;

import hr.kn.jetfighters.JetFighterDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.DatagramPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Main {

    private static String host = "127.0.0.1";
    private static int port = 9956;

    public static void main(String[] args) {
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        NettyUdpClient client = new NettyUdpClient(host, port);

        try {
            ChannelFuture channelFuture = client.start();

            //DatagramSocket socket = new DatagramSocket();
            JetFighterDto dto = new JetFighterDto(22.55f, 17.34f);

            ByteBuf byteBuf = Unpooled.copiedBuffer(serialize(dto));
            client.write(new DatagramPacket(byteBuf, remoteAddress));

            // Wait until the connection is closed.
            channelFuture.channel().closeFuture().sync();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}
