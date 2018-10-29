package hr.kn.jetfighters.server.network.codec;

import com.badlogic.jetfighters.dto.request.GameClientMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

// TODO parse bytes to message in an inteligent way (custom binary, Java serialization, protobuf, json..?)
public class GameClientMessageDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List out) throws Exception {
        byte[] bytes = readBytesFromBuffer(packet.content());
        // System.out.println("Received packet, length: " + bytes.length);
        Object payload = deserialize(bytes);
        if (payload instanceof GameClientMessage) {
            GameClientMessage gameClientMessage = (GameClientMessage) payload;
            gameClientMessage.setSender(packet.sender());
            out.add(gameClientMessage);
        } else {
            System.out.println("Invalid message reached server: " + payload);
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
