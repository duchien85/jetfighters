package com.badlogic.jetfighters.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.model.Jet;
import com.badlogic.jetfighters.model.Missile;

public class JetMovementControls implements KeyPressController<Jet> {

    private JetFightersGame game;

    public JetMovementControls(JetFightersGame game) {
        this.game = game;
    }

    @Override
    public Jet processControls(Input input, Jet jet) {
        Jet newJetLocation = new Jet(jet.getJetId(), jet.getX(), jet.getY());
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            newJetLocation.setY(newJetLocation.getY() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            newJetLocation.setY(newJetLocation.getY() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            newJetLocation.setX(newJetLocation.getX() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            newJetLocation.setX(newJetLocation.getX() + 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && jet.canShoot()) {
            game.client.fireMissile(Missile.fromJet(jet));
            jet.setLastShootTime(System.currentTimeMillis());
        }
        return newJetLocation;
    }
}
