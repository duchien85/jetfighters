package com.badlogic.jetfighters.dto.response;

import java.util.Random;

public class SpawnNewMeteor implements GameServerMessage {
    private float x;
    private float y;

    private static Random random = new Random();

    private SpawnNewMeteor() {
    }

    public static SpawnNewMeteor getInstance() {
        SpawnNewMeteor newMeteor = new SpawnNewMeteor();
        newMeteor.x = random.nextInt(1024);
        newMeteor.y = 768;
        return newMeteor;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
