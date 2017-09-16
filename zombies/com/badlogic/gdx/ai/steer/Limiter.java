/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

public interface Limiter {
    public float getZeroLinearSpeedThreshold();

    public void setZeroLinearSpeedThreshold(float var1);

    public float getMaxLinearSpeed();

    public void setMaxLinearSpeed(float var1);

    public float getMaxLinearAcceleration();

    public void setMaxLinearAcceleration(float var1);

    public float getMaxAngularSpeed();

    public void setMaxAngularSpeed(float var1);

    public float getMaxAngularAcceleration();

    public void setMaxAngularAcceleration(float var1);
}

