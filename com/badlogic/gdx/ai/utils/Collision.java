/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

import com.badlogic.gdx.math.Vector;

public class Collision<T extends Vector<T>> {
    public T point;
    public T normal;

    public Collision(T point, T normal) {
        this.point = point;
        this.normal = normal;
    }

    public Collision<T> set(Collision<T> collision) {
        this.point.set(collision.point);
        this.normal.set(collision.normal);
        return this;
    }

    public Collision<T> set(T point, T normal) {
        this.point.set(point);
        this.normal.set(normal);
        return this;
    }
}

