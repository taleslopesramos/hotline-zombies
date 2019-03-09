/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.GroupBehavior;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;

public class Cohesion<T extends Vector<T>>
extends GroupBehavior<T>
implements Proximity.ProximityCallback<T> {
    private T centerOfMass;

    public Cohesion(Steerable<T> owner, Proximity<T> proximity) {
        super(owner, proximity);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        steering.setZero();
        this.centerOfMass = steering.linear;
        int neighborCount = this.proximity.findNeighbors(this);
        if (neighborCount > 0) {
            this.centerOfMass.scl((float)(1.0f / (float)neighborCount));
            this.centerOfMass.sub(this.owner.getPosition()).nor().scl((float)this.getActualLimiter().getMaxLinearAcceleration());
        }
        return steering;
    }

    @Override
    public boolean reportNeighbor(Steerable<T> neighbor) {
        this.centerOfMass.add(neighbor.getPosition());
        return true;
    }

    @Override
    public Cohesion<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Cohesion<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Cohesion<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

