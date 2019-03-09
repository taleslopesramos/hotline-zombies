/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;

public class FollowFlowField<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected FlowField<T> flowField;
    protected float predictionTime;

    public FollowFlowField(Steerable<T> owner) {
        this(owner, null);
    }

    public FollowFlowField(Steerable<T> owner, FlowField<T> flowField) {
        this(owner, flowField, 0.0f);
    }

    public FollowFlowField(Steerable<T> owner, FlowField<T> flowField, float predictionTime) {
        super(owner);
        this.flowField = flowField;
        this.predictionTime = predictionTime;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        steering.setZero();
        Object location = this.predictionTime == 0.0f ? this.owner.getPosition() : (Object)steering.linear.set(this.owner.getPosition()).mulAdd(this.owner.getLinearVelocity(), (float)this.predictionTime);
        T flowVector = this.flowField.lookup(location);
        if (flowVector != null && !flowVector.isZero()) {
            Limiter actualLimiter = this.getActualLimiter();
            steering.linear.mulAdd(flowVector, (float)actualLimiter.getMaxLinearSpeed()).sub(this.owner.getLinearVelocity()).limit(actualLimiter.getMaxLinearAcceleration());
        }
        return steering;
    }

    public FlowField<T> getFlowField() {
        return this.flowField;
    }

    public FollowFlowField<T> setFlowField(FlowField<T> flowField) {
        this.flowField = flowField;
        return this;
    }

    public float getPredictionTime() {
        return this.predictionTime;
    }

    public FollowFlowField<T> setPredictionTime(float predictionTime) {
        this.predictionTime = predictionTime;
        return this;
    }

    @Override
    public FollowFlowField<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public FollowFlowField<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public FollowFlowField<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    public static interface FlowField<T extends Vector<T>> {
        public T lookup(T var1);
    }

}

