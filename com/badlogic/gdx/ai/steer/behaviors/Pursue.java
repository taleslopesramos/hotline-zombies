/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;

public class Pursue<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Steerable<T> target;
    protected float maxPredictionTime;

    public Pursue(Steerable<T> owner, Steerable<T> target) {
        this(owner, target, 1.0f);
    }

    public Pursue(Steerable<T> owner, Steerable<T> target, float maxPredictionTime) {
        super(owner);
        this.target = target;
        this.maxPredictionTime = maxPredictionTime;
    }

    protected float getActualMaxLinearAcceleration() {
        return this.getActualLimiter().getMaxLinearAcceleration();
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        float squarePredictionTime;
        T targetPosition = this.target.getPosition();
        float squareDistance = steering.linear.set(targetPosition).sub(this.owner.getPosition()).len2();
        float squareSpeed = this.owner.getLinearVelocity().len2();
        float predictionTime = this.maxPredictionTime;
        if (squareSpeed > 0.0f && (squarePredictionTime = squareDistance / squareSpeed) < this.maxPredictionTime * this.maxPredictionTime) {
            predictionTime = (float)Math.sqrt(squarePredictionTime);
        }
        steering.linear.set(targetPosition).mulAdd(this.target.getLinearVelocity(), (float)predictionTime).sub(this.owner.getPosition()).nor().scl((float)this.getActualMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    public Steerable<T> getTarget() {
        return this.target;
    }

    public Pursue<T> setTarget(Steerable<T> target) {
        this.target = target;
        return this;
    }

    public float getMaxPredictionTime() {
        return this.maxPredictionTime;
    }

    public Pursue<T> setMaxPredictionTime(float maxPredictionTime) {
        this.maxPredictionTime = maxPredictionTime;
        return this;
    }

    @Override
    public Pursue<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Pursue<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Pursue<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

