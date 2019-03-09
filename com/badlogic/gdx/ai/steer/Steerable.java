/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public interface Steerable<T extends Vector<T>>
extends Location<T>,
Limiter {
    public T getLinearVelocity();

    public float getAngularVelocity();

    public float getBoundingRadius();

    public boolean isTagged();

    public void setTagged(boolean var1);
}

