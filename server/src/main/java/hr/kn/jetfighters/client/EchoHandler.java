package hr.kn.jetfighters.client;

import hr.kn.jetfighters.JetFighterDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class EchoHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf buf = msg.content();
        Object object = deserialize(buf.array());
        if (object instanceof JetFighterDto) {
            JetFighterDto jet = (JetFighterDto) object;
            System.out.println("Client received jet location " + jet.getX() + ", " + jet.getY());
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}
