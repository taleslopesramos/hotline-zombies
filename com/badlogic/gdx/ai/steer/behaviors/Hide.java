/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class Hide<T extends Vector<T>>
extends Arrive<T>
implements Proximity.ProximityCallback<T> {
    protected Proximity<T> proximity;
    protected float distanceFromBoundary;
    private T toObstacle;
    private T bestHidingSpot;
    private float distance2ToClosest;

    public Hide(Steerable<T> owner) {
        this(owner, null);
    }

    public Hide(Steerable<T> owner, Location<T> target) {
        this(owner, target, null);
    }

    public Hide(Steerable<T> owner, Location<T> target, Proximity<T> proximity) {
        super(owner, target);
        this.proximity = proximity;
        this.bestHidingSpot = this.newVector(owner);
        this.toObstacle = null;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        this.distance2ToClosest = Float.POSITIVE_INFINITY;
        this.toObstacle = steering.linear;
        int neighborsCount = this.proximity.findNeighbors(this);
        return neighborsCount == 0 ? steering.setZero() : this.arrive(steering, this.bestHidingSpot);
    }

    @Override
    public boolean reportNeighbor(Steerable<T> neighbor) {
        Object hidingSpot = this.getHidingPosition(neighbor.getPosition(), neighbor.getBoundingRadius(), this.target.getPosition());
        float distance2 = hidingSpot.dst2(this.owner.getPosition());
        if (distance2 < this.distance2ToClosest) {
            this.distance2ToClosest = distance2;
            this.bestHidingSpot.set(hidingSpot);
            return true;
        }
        return false;
    }

    public Proximity<T> getProximity() {
        return this.proximity;
    }

    public Hide<T> setProximity(Proximity<T> proximity) {
        this.proximity = proximity;
        return this;
    }

    public float getDistanceFromBoundary() {
        return this.distanceFromBoundary;
    }

    public Hide<T> setDistanceFromBoundary(float distanceFromBoundary) {
        this.distanceFromBoundary = distanceFromBoundary;
        return this;
    }

    protected T getHidingPosition(T obstaclePosition, float obstacleRadius, T targetPosition) {
        float distanceAway = obstacleRadius + this.distanceFromBoundary;
        this.toObstacle.set(obstaclePosition).sub(targetPosition).nor();
        return this.toObstacle.scl((float)distanceAway).add(obstaclePosition);
    }

    @Override
    public Hide<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Hide<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Hide<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Hide<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public Hide<T> setArrivalTolerance(float arrivalTolerance) {
        this.arrivalTolerance = arrivalTolerance;
        return this;
    }

    @Override
    public Hide<T> setDecelerationRadius(float decelerationRadius) {
        this.decelerationRadius = decelerationRadius;
        return this;
    }

    @Override
    public Hide<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }
}

