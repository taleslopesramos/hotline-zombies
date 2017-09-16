/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.limiters;

import com.badlogic.gdx.ai.steer.limiters.NullLimiter;

public class AngularSpeedLimiter
extends NullLimiter {
    private float maxAngularSpeed;

    public AngularSpeedLimiter(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }
}

