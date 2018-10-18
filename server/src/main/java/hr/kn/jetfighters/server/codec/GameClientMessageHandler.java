package hr.kn.jetfighters.server.codec;

import com.badlogic.jetfighters.dto.request.GameClientMessage;
import com.badlogic.jetfighters.dto.request.JetMoveMessage;
import com.badlogic.jetfighters.dto.request.JoinGameMessage;
import com.badlogic.jetfighters.dto.response.JoinGameMessageResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

// TODO Process message delegating to services, managers, repos etc.
public class GameClientMessageHandler extends SimpleChannelInboundHandler<GameClientMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameClientMessage msg) {
        if (msg instanceof JoinGameMessage) {
            JoinGameMessage joinMsg = (JoinGameMessage) msg;
            System.out.println("Player " + joinMsg.getDesiredJetId() + " is requesting to join a game.");
            JoinGameMessageResponse response = new JoinGameMessageResponse("SUCCESS");
            ByteBuf bufResponse = Unpooled.copiedBuffer(serialize((response)));
            ctx.channel().writeAndFlush(new DatagramPacket(bufResponse, joinMsg.getSender()));
        } else if (msg instanceof JetMoveMessage) {
            JetMoveMessage moveMsg = (JetMoveMessage) msg;
            System.out.println("Jet " + moveMsg.getJetId() + " at location " + moveMsg.getX() + ", " + moveMsg.getY());
        }
    }

    private byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }
}
