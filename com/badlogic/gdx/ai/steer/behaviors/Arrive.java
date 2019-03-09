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

public class Arrive<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Location<T> target;
    protected float arrivalTolerance;
    protected float decelerationRadius;
    protected float timeToTarget = 0.1f;

    public Arrive(Steerable<T> owner) {
        this(owner, null);
    }

    public Arrive(Steerable<T> owner, Location<T> target) {
        super(owner);
        this.target = target;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        return this.arrive(steering, this.target.getPosition());
    }

    protected SteeringAcceleration<T> arrive(SteeringAcceleration<T> steering, T targetPosition) {
        float toTarget = steering.linear.set(targetPosition).sub(this.owner.getPosition());
        float distance = toTarget.len();
        if (distance <= this.arrivalTolerance) {
            return steering.setZero();
        }
        Limiter actualLimiter = this.getActualLimiter();
        float targetSpeed = actualLimiter.getMaxLinearSpeed();
        if (distance <= this.decelerationRadius) {
            targetSpeed *= distance / this.decelerationRadius;
        }
        float targetVelocity = toTarget.scl((float)(targetSpeed / distance));
        targetVelocity.sub(this.owner.getLinearVelocity()).scl((float)(1.0f / this.timeToTarget)).limit(actualLimiter.getMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    public Location<T> getTarget() {
        return this.target;
    }

    public Arrive<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    public float getArrivalTolerance() {
        return this.arrivalTolerance;
    }

    public Arrive<T> setArrivalTolerance(float arrivalTolerance) {
        this.arrivalTolerance = arrivalTolerance;
        return this;
    }

    public float getDecelerationRadius() {
        return this.decelerationRadius;
    }

    public Arrive<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    public float getTimeToTarget() {
        return this.timeToTarget;
    }

    public Arrive<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }

    @Override
    public Arrive<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Arrive<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Arrive<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

