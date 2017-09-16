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
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class Separation<T extends Vector<T>>
extends GroupBehavior<T>
implements Proximity.ProximityCallback<T> {
    float decayCoefficient = 1.0f;
    private T toAgent;
    private T linear;

    public Separation(Steerable<T> owner, Proximity<T> proximity) {
        super(owner, proximity);
        this.toAgent = this.newVector(owner);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        steering.setZero();
        this.linear = steering.linear;
        this.proximity.findNeighbors(this);
        return steering;
    }

    @Override
    public boolean reportNeighbor(Steerable<T> neighbor) {
        this.toAgent.set(this.owner.getPosition()).sub(neighbor.getPosition());
        float distanceSqr = this.toAgent.len2();
        float maxAcceleration = this.getActualLimiter().getMaxLinearAcceleration();
        float strength = this.getDecayCoefficient() / distanceSqr;
        if (strength > maxAcceleration) {
            strength = maxAcceleration;
        }
        this.linear.mulAdd(this.toAgent, (float)(strength / (float)Math.sqrt(distanceSqr)));
        return true;
    }

    public float getDecayCoefficient() {
        return this.decayCoefficient;
    }

    public Separation<T> setDecayCoefficient(float decayCoefficient) {
        this.decayCoefficient = decayCoefficient;
        return this;
    }

    @Override
    public Separation<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Separation<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Separation<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

