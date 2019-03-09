/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

import com.badlogic.gdx.math.Vector;

public interface Location<T extends Vector<T>> {
    public T getPosition();

    public float getOrientation();

    public void setOrientation(float var1);

    public float vectorToAngle(T var1);

    public T angleToVector(T var1, float var2);

    public Location<T> newLocation();
}

