package hr.kn.jetfighters.server.game.manager;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;

import java.util.Map;

public class ChannelManager {
    private Map<String, Channel> channels = Maps.newHashMap();

    public Map<String, Channel> getChannels() {
        return channels;
    }
}
