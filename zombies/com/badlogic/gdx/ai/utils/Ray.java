/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

import com.badlogic.gdx.math.Vector;

public class Ray<T extends Vector<T>> {
    public T start;
    public T end;

    public Ray(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public Ray<T> set(Ray<T> ray) {
        this.start.set(ray.start);
        this.end.set(ray.end);
        return this;
    }

    public Ray<T> set(T start, T end) {
        this.start.set(start);
        this.end.set(end);
        return this;
    }
}

