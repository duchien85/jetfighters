package hr.kn.jetfighters.server.eventbus.listener;

import com.badlogic.jetfighters.dto.request.JetMoveMessage;
import com.google.common.eventbus.Subscribe;

public class JetMoveMessageListener implements ServerMessageListener<JetMoveMessage> {

    @Subscribe
    @Override
    public void handle(JetMoveMessage message) {
        System.out.println("Jet " + message.getJetId() + " at location " + message.getX() + ", " + message.getY());
    }

}
