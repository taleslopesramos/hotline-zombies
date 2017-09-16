/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public abstract class SteeringBehavior<T extends Vector<T>> {
    protected Steerable<T> owner;
    protected Limiter limiter;
    protected boolean enabled;

    public SteeringBehavior(Steerable<T> owner) {
        this(owner, null, true);
    }

    public SteeringBehavior(Steerable<T> owner, Limiter limiter) {
        this(owner, limiter, true);
    }

    public SteeringBehavior(Steerable<T> owner, boolean enabled) {
        this(owner, null, enabled);
    }

    public SteeringBehavior(Steerable<T> owner, Limiter limiter, boolean enabled) {
        this.owner = owner;
        this.limiter = limiter;
        this.enabled = enabled;
    }

    public SteeringAcceleration<T> calculateSteering(SteeringAcceleration<T> steering) {
        return this.isEnabled() ? this.calculateRealSteering(steering) : steering.setZero();
    }

    protected abstract SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> var1);

    public Steerable<T> getOwner() {
        return this.owner;
    }

    public SteeringBehavior<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    public Limiter getLimiter() {
        return this.limiter;
    }

    public SteeringBehavior<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public SteeringBehavior<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    protected Limiter getActualLimiter() {
        return this.limiter == null ? this.owner : this.limiter;
    }

    protected T newVector(Location<T> location) {
        return location.getPosition().cpy().setZero();
    }
}

