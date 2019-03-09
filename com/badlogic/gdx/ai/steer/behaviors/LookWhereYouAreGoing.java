/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class LookWhereYouAreGoing<T extends Vector<T>>
extends ReachOrientation<T> {
    public LookWhereYouAreGoing(Steerable<T> owner) {
        super(owner);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        if (this.owner.getLinearVelocity().isZero(this.getActualLimiter().getZeroLinearSpeedThreshold())) {
            return steering.setZero();
        }
        float orientation = this.owner.vectorToAngle(this.owner.getLinearVelocity());
        return this.reachOrientation(steering, orientation);
    }

    @Override
    public LookWhereYouAreGoing<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public LookWhereYouAreGoing<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public LookWhereYouAreGoing<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public LookWhereYouAreGoing<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public LookWhereYouAreGoing<T> setAlignTolerance(float alignTolerance) {
        this.alignTolerance = alignTolerance;
        return this;
    }

    @Override
    public LookWhereYouAreGoing<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    @Override
    public LookWhereYouAreGoing<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }
}

