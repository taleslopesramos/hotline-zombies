/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.limiters;

import com.badlogic.gdx.ai.steer.limiters.NullLimiter;

public class LinearLimiter
extends NullLimiter {
    private float maxLinearAcceleration;
    private float maxLinearSpeed;

    public LinearLimiter(float maxLinearAcceleration, float maxLinearSpeed) {
        this.maxLinearAcceleration = maxLinearAcceleration;
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }
}

