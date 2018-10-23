package com.badlogic.jetfighters.screens;

import com.badlogic.gdx.Input;

public class JetIdInputListener implements Input.TextInputListener {

    private MainMenuScreen screen;

    public JetIdInputListener(MainMenuScreen screen) {
        this.screen = screen;
    }

    @Override
    public void input(String jetId) {
        System.out.println("Entered JetId is " + jetId);
        this.screen.jetId = jetId;
        proceedWithMainMenu();
    }

    @Override
    public void canceled() {
        System.out.println("Using default JetId");
        proceedWithMainMenu();
    }

    private void proceedWithMainMenu() {
        this.screen.jetIdPicked = true;
        this.screen.lastConnectionAttemptTimestamp = System.currentTimeMillis();
    }
}