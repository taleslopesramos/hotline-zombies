/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class Seek<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Location<T> target;

    public Seek(Steerable<T> owner) {
        this(owner, null);
    }

    public Seek(Steerable<T> owner, Location<T> target) {
        super(owner);
        this.target = target;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        steering.linear.set(this.target.getPosition()).sub(this.owner.getPosition()).nor().scl((float)this.getActualLimiter().getMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    public Location<T> getTarget() {
        return this.target;
    }

    public Seek<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public Seek<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Seek<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Seek<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

