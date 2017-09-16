/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;

public class MatchVelocity<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Steerable<T> target;
    protected float timeToTarget;

    public MatchVelocity(Steerable<T> owner) {
        this(owner, null);
    }

    public MatchVelocity(Steerable<T> owner, Steerable<T> target) {
        this(owner, target, 0.1f);
    }

    public MatchVelocity(Steerable<T> owner, Steerable<T> target, float timeToTarget) {
        super(owner);
        this.target = target;
        this.timeToTarget = timeToTarget;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        steering.linear.set(this.target.getLinearVelocity()).sub(this.owner.getLinearVelocity()).scl((float)(1.0f / this.timeToTarget)).limit(this.getActualLimiter().getMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    public Steerable<T> getTarget() {
        return this.target;
    }

    public MatchVelocity<T> setTarget(Steerable<T> target) {
        this.target = target;
        return this;
    }

    public float getTimeToTarget() {
        return this.timeToTarget;
    }

    public MatchVelocity<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }

    @Override
    public MatchVelocity<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public MatchVelocity<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public MatchVelocity<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

