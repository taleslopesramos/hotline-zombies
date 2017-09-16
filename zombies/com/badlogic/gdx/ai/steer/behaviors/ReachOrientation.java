/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.ArithmeticUtils;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class ReachOrientation<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Location<T> target;
    protected float alignTolerance;
    protected float decelerationRadius;
    protected float timeToTarget = 0.1f;

    public ReachOrientation(Steerable<T> owner) {
        this(owner, null);
    }

    public ReachOrientation(Steerable<T> owner, Location<T> target) {
        super(owner);
        this.target = target;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        return this.reachOrientation(steering, this.target.getOrientation());
    }

    protected SteeringAcceleration<T> reachOrientation(SteeringAcceleration<T> steering, float targetOrientation) {
        float angularAcceleration;
        float rotationSize;
        float rotation = ArithmeticUtils.wrapAngleAroundZero(targetOrientation - this.owner.getOrientation());
        float f = rotationSize = rotation < 0.0f ? - rotation : rotation;
        if (rotationSize <= this.alignTolerance) {
            return steering.setZero();
        }
        Limiter actualLimiter = this.getActualLimiter();
        float targetRotation = actualLimiter.getMaxAngularSpeed();
        if (rotationSize <= this.decelerationRadius) {
            targetRotation *= rotationSize / this.decelerationRadius;
        }
        steering.angular = ((targetRotation *= rotation / rotationSize) - this.owner.getAngularVelocity()) / this.timeToTarget;
        float f2 = angularAcceleration = steering.angular < 0.0f ? - steering.angular : steering.angular;
        if (angularAcceleration > actualLimiter.getMaxAngularAcceleration()) {
            steering.angular *= actualLimiter.getMaxAngularAcceleration() / angularAcceleration;
        }
        steering.linear.setZero();
        return steering;
    }

    public Location<T> getTarget() {
        return this.target;
    }

    public ReachOrientation<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    public float getAlignTolerance() {
        return this.alignTolerance;
    }

    public ReachOrientation<T> setAlignTolerance(float alignTolerance) {
        this.alignTolerance = alignTolerance;
        return this;
    }

    public float getDecelerationRadius() {
        return this.decelerationRadius;
    }

    public ReachOrientation<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    public float getTimeToTarget() {
        return this.timeToTarget;
    }

    public ReachOrientation<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }

    @Override
    public ReachOrientation<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public ReachOrientation<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public ReachOrientation<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

