package com.badlogic.jetfighters.render;

import com.badlogic.gdx.utils.Array;

public interface Renderer<T> {
    void render(Array<T> renderable);
}
