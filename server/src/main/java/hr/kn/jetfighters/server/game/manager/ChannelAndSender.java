package hr.kn.jetfighters.server.game.manager;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public class ChannelAndSender {
    private Channel channel;
    private InetSocketAddress sender;

    public ChannelAndSender(Channel channel, InetSocketAddress sender) {
        this.channel = channel;
        this.sender = sender;
    }

    public Channel getChannel() {
        return channel;
    }

    public InetSocketAddress getSender() {
        return sender;
    }
}
