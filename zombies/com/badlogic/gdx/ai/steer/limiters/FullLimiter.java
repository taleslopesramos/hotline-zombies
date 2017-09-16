/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.limiters;

import com.badlogic.gdx.ai.steer.Limiter;

public class FullLimiter
implements Limiter {
    private float maxLinearAcceleration;
    private float maxLinearSpeed;
    private float maxAngularAcceleration;
    private float maxAngularSpeed;
    private float zeroLinearSpeedThreshold;

    public FullLimiter(float maxLinearAcceleration, float maxLinearSpeed, float maxAngularAcceleration, float maxAngularSpeed) {
        this.maxLinearAcceleration = maxLinearAcceleration;
        this.maxLinearSpeed = maxLinearSpeed;
        this.maxAngularAcceleration = maxAngularAcceleration;
        this.maxAngularSpeed = maxAngularSpeed;
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

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return this.zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float zeroLinearSpeedThreshold) {
        this.zeroLinearSpeedThreshold = zeroLinearSpeedThreshold;
    }
}

