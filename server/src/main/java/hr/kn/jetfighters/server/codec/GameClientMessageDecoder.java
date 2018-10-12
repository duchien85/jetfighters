package hr.kn.jetfighters.server.codec;

import com.badlogic.jetfighters.dto.JetMoveMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class GameClientMessageDecoder extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        DatagramPacket packet = (DatagramPacket) msg;
        byte[] bytes = readBytesFromBuffer(packet.content());
        Object payload = deserialize(bytes);
        if (payload instanceof JetMoveMessage) {
            // TODO parse bytes to message in an inteligent way (custom binary, Java serialization, protobuf, json..?)
            JetMoveMessage dto = (JetMoveMessage) payload;
            // TODO Process message delegating to services, managers, repos etc.
            System.out.println("Jet is at location: " + dto.getX() + ", " + dto.getY());
        }
    }

    private byte[] readBytesFromBuffer(ByteBuf in) {
        int readableBytes = in.readableBytes();
        byte[] bytes = new byte[readableBytes];
        in.readBytes(bytes);
        return bytes;
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
