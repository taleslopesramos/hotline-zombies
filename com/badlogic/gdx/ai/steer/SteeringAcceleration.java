/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.math.Vector;

public class SteeringAcceleration<T extends Vector<T>> {
    public T linear;
    public float angular;

    public SteeringAcceleration(T linear) {
        this(linear, 0.0f);
    }

    public SteeringAcceleration(T linear, float angular) {
        if (linear == null) {
            throw new IllegalArgumentException("Linear acceleration cannot be null");
        }
        this.linear = linear;
        this.angular = angular;
    }

    public boolean isZero() {
        return this.angular == 0.0f && this.linear.isZero();
    }

    public SteeringAcceleration<T> setZero() {
        this.linear.setZero();
        this.angular = 0.0f;
        return this;
    }

    public SteeringAcceleration<T> add(SteeringAcceleration<T> steering) {
        this.linear.add(steering.linear);
        this.angular += steering.angular;
        return this;
    }

    public SteeringAcceleration<T> scl(float scalar) {
        this.linear.scl((float)scalar);
        this.angular *= scalar;
        return this;
    }

    public SteeringAcceleration<T> mulAdd(SteeringAcceleration<T> steering, float scalar) {
        this.linear.mulAdd(steering.linear, (float)scalar);
        this.angular += steering.angular * scalar;
        return this;
    }

    public float calculateSquareMagnitude() {
        return this.linear.len2() + this.angular * this.angular;
    }

    public float calculateMagnitude() {
        return (float)Math.sqrt(this.calculateSquareMagnitude());
    }
}

