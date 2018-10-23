package hr.kn.jetfighters.server.game.manager;

import com.google.common.collect.Maps;

import java.util.Map;

public class ChannelManager {
    private Map<String, ChannelAndSender> channels = Maps.newHashMap();

    public Map<String, ChannelAndSender> getChannels() {
        return channels;
    }
}
