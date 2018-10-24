package hr.kn.jetfighters.server.network.timer;

import com.badlogic.jetfighters.dto.response.SpawnNewMeteor;
import com.badlogic.jetfighters.dto.serialization.GameMessageSerde;
import hr.kn.jetfighters.server.game.GameState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnMeteorTimer {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public SpawnMeteorTimer() {
        executorService.scheduleAtFixedRate(() -> {
            SpawnNewMeteor newMeteor = SpawnNewMeteor.getInstance();
            ByteBuf bufResponse = Unpooled.copiedBuffer(GameMessageSerde.serialize((newMeteor)));
            GameState.channelManager.getChannels().forEach((s, channelAndSender) -> {
                bufResponse.retain();
                channelAndSender.getChannel().writeAndFlush(new DatagramPacket(bufResponse, channelAndSender.getSender()));
            });
        }, 0, 2000, TimeUnit.MILLISECONDS);
    }

}
