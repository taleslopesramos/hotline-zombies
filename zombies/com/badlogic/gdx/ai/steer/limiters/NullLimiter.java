/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.limiters;

import com.badlogic.gdx.ai.steer.Limiter;

public class NullLimiter
implements Limiter {
    public static final NullLimiter NEUTRAL_LIMITER = new NullLimiter(){

        @Override
        public float getMaxLinearSpeed() {
            return Float.POSITIVE_INFINITY;
        }

        @Override
        public float getMaxLinearAcceleration() {
            return Float.POSITIVE_INFINITY;
        }

        @Override
        public float getMaxAngularSpeed() {
            return Float.POSITIVE_INFINITY;
        }

        @Override
        public float getMaxAngularAcceleration() {
            return Float.POSITIVE_INFINITY;
        }
    };

    @Override
    public float getMaxLinearSpeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxLinearAcceleration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxAngularSpeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxAngularAcceleration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float zeroLinearSpeedThreshold) {
        throw new UnsupportedOperationException();
    }

}

