package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.dto.JetMoveMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.DatagramPacket;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UdpClientTest {

    private final int port = 9956;
    private final UdpClient client = new UdpClient();

    @Test
    public void testSendJetMoveMessage() throws InterruptedException, IOException {
        Channel channel = client.start();
        String host = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        JetMoveMessage dto = new JetMoveMessage(22.55f, 17.34f);
        ByteBuf byteBuf = Unpooled.copiedBuffer(serialize(dto));
        ChannelFuture channelFuture = channel.writeAndFlush(new DatagramPacket(byteBuf, remoteAddress)).sync();
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("Writing to channel success!");
            }
        });
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}
