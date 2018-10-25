package com.badlogic.jetfighters.controls;

import com.badlogic.gdx.Input;

public interface KeyPressController<T> {
    T processControls(Input input, T renderable);
}
