package com.badlogic.jetfighters.collision;

public interface CollisionDetector<T, Z> {
    void collideAndRemove(T t, Z z);
}
