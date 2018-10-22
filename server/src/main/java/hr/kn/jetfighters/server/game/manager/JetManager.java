package hr.kn.jetfighters.server.game.manager;

import com.badlogic.jetfighters.model.Jet;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

public class JetManager {
    private Map<String, Jet> jets = Maps.newHashMap();

    public Map<String, Jet> getJets() {
        return jets;
    }

    public Optional<Jet> findJetById(String jetId) {
        return Optional.ofNullable(jets.get(jetId));
    }

    public void updateJetLocation(String jetId, float x, float y) {
        Optional<Jet> jetMb = findJetById(jetId);
        jetMb.ifPresent(jet -> {
            jet.setX(x);
            jet.setY(y);
        });
    }
}
