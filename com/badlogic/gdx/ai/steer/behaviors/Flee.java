/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class Flee<T extends Vector<T>>
extends Seek<T> {
    public Flee(Steerable<T> owner) {
        this(owner, null);
    }

    public Flee(Steerable<T> owner, Location<T> target) {
        super(owner, target);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        steering.linear.set(this.owner.getPosition()).sub(this.target.getPosition()).nor().scl((float)this.getActualLimiter().getMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    @Override
    public Flee<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Flee<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Flee<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Flee<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }
}

