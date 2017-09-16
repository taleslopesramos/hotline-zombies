/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector;

public class Evade<T extends Vector<T>>
extends Pursue<T> {
    public Evade(Steerable<T> owner, Steerable<T> target) {
        this(owner, target, 1.0f);
    }

    public Evade(Steerable<T> owner, Steerable<T> target, float maxPredictionTime) {
        super(owner, target, maxPredictionTime);
    }

    @Override
    protected float getActualMaxLinearAcceleration() {
        return - this.getActualLimiter().getMaxLinearAcceleration();
    }

    @Override
    public Evade<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Evade<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Evade<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Evade<T> setTarget(Steerable<T> target) {
        this.target = target;
        return this;
    }
}

