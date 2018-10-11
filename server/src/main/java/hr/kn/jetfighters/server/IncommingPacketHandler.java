package hr.kn.jetfighters.server;

import hr.kn.jetfighters.JetFighterDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;


public class IncommingPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger LOGGER = LoggerFactory.getLogger(IncommingPacketHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws IOException, ClassNotFoundException {
        final InetAddress sourceAddress = packet.sender().getAddress();
        final ByteBuf buf = packet.content();
        final int rcvPktLength = buf.readableBytes();
        final byte[] rcvPktBuf = new byte[rcvPktLength];
        buf.readBytes(rcvPktBuf);
        Object msg = deserialize(rcvPktBuf);

        LOGGER.info("Packet length: {}", rcvPktLength);
        if (msg instanceof JetFighterDto) {
            JetFighterDto dto = (JetFighterDto) msg;
            LOGGER.info("JetFighter from client [{}]: x,y: {}, {}", sourceAddress.toString(), dto.getX(), dto.getY());
//            ctx.channel().write("hello".getBytes());
            ctx.writeAndFlush("hello".getBytes());
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
