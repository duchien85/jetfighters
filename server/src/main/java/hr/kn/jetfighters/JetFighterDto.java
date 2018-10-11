package hr.kn.jetfighters;

import java.io.Serializable;

public class JetFighterDto implements Serializable {
    private float x;
    private float y;

    public JetFighterDto(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
