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

public class Face<T extends Vector<T>>
extends ReachOrientation<T> {
    public Face(Steerable<T> owner) {
        this(owner, null);
    }

    public Face(Steerable<T> owner, Location<T> target) {
        super(owner, target);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        return this.face(steering, this.target.getPosition());
    }

    protected SteeringAcceleration<T> face(SteeringAcceleration<T> steering, T targetPosition) {
        Object toTarget = steering.linear.set(targetPosition).sub(this.owner.getPosition());
        if (toTarget.isZero(this.getActualLimiter().getZeroLinearSpeedThreshold())) {
            return steering.setZero();
        }
        float orientation = this.owner.vectorToAngle(toTarget);
        return this.reachOrientation(steering, orientation);
    }

    @Override
    public Face<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Face<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Face<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Face<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public Face<T> setAlignTolerance(float alignTolerance) {
        this.alignTolerance = alignTolerance;
        return this;
    }

    @Override
    public Face<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    @Override
    public Face<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }
}

