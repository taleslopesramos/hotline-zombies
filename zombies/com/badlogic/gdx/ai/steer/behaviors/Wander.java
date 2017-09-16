/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;

public class Wander<T extends Vector<T>>
extends Face<T> {
    protected float wanderOffset;
    protected float wanderRadius;
    protected float wanderRate;
    protected float lastTime;
    protected float wanderOrientation;
    protected boolean faceEnabled;
    private T internalTargetPosition;
    private T wanderCenter;

    public Wander(Steerable<T> owner) {
        super(owner);
        this.internalTargetPosition = this.newVector(owner);
        this.wanderCenter = this.newVector(owner);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        float now = GdxAI.getTimepiece().getTime();
        if (this.lastTime > 0.0f) {
            float delta = now - this.lastTime;
            this.wanderOrientation += MathUtils.randomTriangular(this.wanderRate * delta);
        }
        this.lastTime = now;
        float targetOrientation = this.wanderOrientation + this.owner.getOrientation();
        this.wanderCenter.set(this.owner.getPosition()).mulAdd(this.owner.angleToVector(steering.linear, this.owner.getOrientation()), (float)this.wanderOffset);
        this.internalTargetPosition.set(this.wanderCenter).mulAdd(this.owner.angleToVector(steering.linear, targetOrientation), (float)this.wanderRadius);
        float maxLinearAcceleration = this.getActualLimiter().getMaxLinearAcceleration();
        if (this.faceEnabled) {
            this.face(steering, this.internalTargetPosition);
            this.owner.angleToVector(steering.linear, this.owner.getOrientation()).scl((float)maxLinearAcceleration);
        } else {
            steering.linear.set(this.internalTargetPosition).sub(this.owner.getPosition()).nor().scl((float)maxLinearAcceleration);
            steering.angular = 0.0f;
        }
        return steering;
    }

    public float getWanderOffset() {
        return this.wanderOffset;
    }

    public Wander<T> setWanderOffset(float wanderOffset) {
        this.wanderOffset = wanderOffset;
        return this;
    }

    public float getWanderRadius() {
        return this.wanderRadius;
    }

    public Wander<T> setWanderRadius(float wanderRadius) {
        this.wanderRadius = wanderRadius;
        return this;
    }

    public float getWanderRate() {
        return this.wanderRate;
    }

    public Wander<T> setWanderRate(float wanderRate) {
        this.wanderRate = wanderRate;
        return this;
    }

    public float getWanderOrientation() {
        return this.wanderOrientation;
    }

    public Wander<T> setWanderOrientation(float wanderOrientation) {
        this.wanderOrientation = wanderOrientation;
        return this;
    }

    public boolean isFaceEnabled() {
        return this.faceEnabled;
    }

    public Wander<T> setFaceEnabled(boolean faceEnabled) {
        this.faceEnabled = faceEnabled;
        return this;
    }

    public T getInternalTargetPosition() {
        return this.internalTargetPosition;
    }

    public T getWanderCenter() {
        return this.wanderCenter;
    }

    @Override
    public Wander<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Wander<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Wander<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Wander<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public Wander<T> setAlignTolerance(float alignTolerance) {
        this.alignTolerance = alignTolerance;
        return this;
    }

    @Override
    public Wander<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    @Override
    public Wander<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }
}

