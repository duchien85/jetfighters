package hr.kn.jetfighters.server.game;

import hr.kn.jetfighters.server.game.manager.ChannelManager;
import hr.kn.jetfighters.server.game.manager.JetManager;

public class GameState {
    public static JetManager jetManager = new JetManager();
    public static ChannelManager channelManager = new ChannelManager();
}
