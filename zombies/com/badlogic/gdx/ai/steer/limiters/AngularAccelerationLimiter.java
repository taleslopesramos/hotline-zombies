/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.limiters;

import com.badlogic.gdx.ai.steer.limiters.NullLimiter;

public class AngularAccelerationLimiter
extends NullLimiter {
    private float maxAngularAcceleration;

    public AngularAccelerationLimiter(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }
}

