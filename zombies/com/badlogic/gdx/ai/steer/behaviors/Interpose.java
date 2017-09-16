/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class Interpose<T extends Vector<T>>
extends Arrive<T> {
    protected Steerable<T> agentA;
    protected Steerable<T> agentB;
    protected float interpositionRatio;
    private T internalTargetPosition;

    public Interpose(Steerable<T> owner, Steerable<T> agentA, Steerable<T> agentB) {
        this(owner, agentA, agentB, 0.5f);
    }

    public Interpose(Steerable<T> owner, Steerable<T> agentA, Steerable<T> agentB, float interpositionRatio) {
        super(owner);
        this.agentA = agentA;
        this.agentB = agentB;
        this.interpositionRatio = interpositionRatio;
        this.internalTargetPosition = this.newVector(owner);
    }

    public Steerable<T> getAgentA() {
        return this.agentA;
    }

    public Interpose<T> setAgentA(Steerable<T> agentA) {
        this.agentA = agentA;
        return this;
    }

    public Steerable<T> getAgentB() {
        return this.agentB;
    }

    public Interpose<T> setAgentB(Steerable<T> agentB) {
        this.agentB = agentB;
        return this;
    }

    public float getInterpositionRatio() {
        return this.interpositionRatio;
    }

    public Interpose<T> setInterpositionRatio(float interpositionRatio) {
        this.interpositionRatio = interpositionRatio;
        return this;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        this.internalTargetPosition.set(this.agentB.getPosition()).sub(this.agentA.getPosition()).scl((float)this.interpositionRatio).add(this.agentA.getPosition());
        float timeToTargetPosition = this.owner.getPosition().dst(this.internalTargetPosition) / this.getActualLimiter().getMaxLinearSpeed();
        steering.linear.set(this.agentA.getPosition()).mulAdd(this.agentA.getLinearVelocity(), (float)timeToTargetPosition);
        this.internalTargetPosition.set(this.agentB.getPosition()).mulAdd(this.agentB.getLinearVelocity(), (float)timeToTargetPosition);
        this.internalTargetPosition.sub(steering.linear).scl((float)this.interpositionRatio).add(steering.linear);
        return this.arrive(steering, this.internalTargetPosition);
    }

    public T getInternalTargetPosition() {
        return this.internalTargetPosition;
    }

    @Override
    public Interpose<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Interpose<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Interpose<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Interpose<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public Interpose<T> setArrivalTolerance(float arrivalTolerance) {
        this.arrivalTolerance = arrivalTolerance;
        return this;
    }

    @Override
    public Interpose<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    @Override
    public Interpose<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }
}

