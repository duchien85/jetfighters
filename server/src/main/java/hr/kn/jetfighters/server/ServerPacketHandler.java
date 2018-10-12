package hr.kn.jetfighters.server;

import hr.kn.jetfighters.JetFighterDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;


public class ServerPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger LOGGER = LoggerFactory.getLogger(ServerPacketHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws IOException, ClassNotFoundException {
        final InetAddress sourceAddress = packet.sender().getAddress();
        final ByteBuf buf = packet.content();
        final int rcvPktLength = buf.readableBytes();
        final byte[] rcvPktBuf = new byte[rcvPktLength];
        buf.readBytes(rcvPktBuf);
        Object msg = deserialize(rcvPktBuf);

        LOGGER.info("Received packet, length: {}", rcvPktLength);
        if (msg instanceof JetFighterDto) {
            JetFighterDto dto = (JetFighterDto) msg;
            LOGGER.info("JetFighter from client [{}]: x,y: {}, {}", sourceAddress.toString(), dto.getX(), dto.getY());
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(serialize("SUCCESS")), packet.sender()));
        }
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
