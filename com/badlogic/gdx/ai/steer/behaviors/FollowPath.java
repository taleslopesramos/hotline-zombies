/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class FollowPath<T extends Vector<T>, P extends Path.PathParam>
extends Arrive<T> {
    protected Path<T, P> path;
    protected float pathOffset;
    protected P pathParam;
    protected boolean arriveEnabled;
    protected float predictionTime;
    private T internalTargetPosition;

    public FollowPath(Steerable<T> owner, Path<T, P> path) {
        this(owner, path, 0.0f);
    }

    public FollowPath(Steerable<T> owner, Path<T, P> path, float pathOffset) {
        this(owner, path, pathOffset, 0.0f);
    }

    public FollowPath(Steerable<T> owner, Path<T, P> path, float pathOffset, float predictionTime) {
        super(owner);
        this.path = path;
        this.pathParam = path.createParam();
        this.pathOffset = pathOffset;
        this.predictionTime = predictionTime;
        this.arriveEnabled = true;
        this.internalTargetPosition = this.newVector(owner);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        Object location = this.predictionTime == 0.0f ? this.owner.getPosition() : (Object)steering.linear.set(this.owner.getPosition()).mulAdd(this.owner.getLinearVelocity(), (float)this.predictionTime);
        float distance = this.path.calculateDistance(location, this.pathParam);
        float targetDistance = distance + this.pathOffset;
        this.path.calculateTargetPosition(this.internalTargetPosition, this.pathParam, targetDistance);
        if (this.arriveEnabled && this.path.isOpen() && (this.pathOffset >= 0.0f ? targetDistance > this.path.getLength() - this.decelerationRadius : targetDistance < this.decelerationRadius)) {
            return this.arrive(steering, this.internalTargetPosition);
        }
        steering.linear.set(this.internalTargetPosition).sub(this.owner.getPosition()).nor().scl((float)this.getActualLimiter().getMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    public Path<T, P> getPath() {
        return this.path;
    }

    public FollowPath<T, P> setPath(Path<T, P> path) {
        this.path = path;
        return this;
    }

    public float getPathOffset() {
        return this.pathOffset;
    }

    public boolean isArriveEnabled() {
        return this.arriveEnabled;
    }

    public float getPredictionTime() {
        return this.predictionTime;
    }

    public FollowPath<T, P> setPredictionTime(float predictionTime) {
        this.predictionTime = predictionTime;
        return this;
    }

    public FollowPath<T, P> setArriveEnabled(boolean arriveEnabled) {
        this.arriveEnabled = arriveEnabled;
        return this;
    }

    public FollowPath<T, P> setPathOffset(float pathOffset) {
        this.pathOffset = pathOffset;
        return this;
    }

    public P getPathParam() {
        return this.pathParam;
    }

    public T getInternalTargetPosition() {
        return this.internalTargetPosition;
    }

    public FollowPath<T, P> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public FollowPath<T, P> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public FollowPath<T, P> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    public FollowPath<T, P> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    public FollowPath<T, P> setArrivalTolerance(float arrivalTolerance) {
        this.arrivalTolerance = arrivalTolerance;
        return this;
    }

    public FollowPath<T, P> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    public FollowPath<T, P> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }
}

