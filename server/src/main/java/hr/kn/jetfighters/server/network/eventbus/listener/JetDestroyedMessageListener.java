package hr.kn.jetfighters.server.network.eventbus.listener;

import com.badlogic.jetfighters.dto.request.JetDestroyedMessage;
import com.google.common.eventbus.Subscribe;
import hr.kn.jetfighters.server.game.GameState;

public class JetDestroyedMessageListener implements ServerMessageListener<JetDestroyedMessage> {

    @Subscribe
    @Override
    public void handle(JetDestroyedMessage message) {
        System.out.println("Died jet: " + message.getJetId());
        GameState.channelManager.getChannels().remove(message.getJetId());
        GameState.jetManager.getJets().remove(message.getJetId());
    }

}
