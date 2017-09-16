/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.limiters;

import com.badlogic.gdx.ai.steer.limiters.NullLimiter;

public class LinearSpeedLimiter
extends NullLimiter {
    private float maxLinearSpeed;

    public LinearSpeedLimiter(float maxLinearSpeed) {
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
}

