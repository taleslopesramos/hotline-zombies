/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.GroupBehavior;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class CollisionAvoidance<T extends Vector<T>>
extends GroupBehavior<T>
implements Proximity.ProximityCallback<T> {
    private float shortestTime;
    private Steerable<T> firstNeighbor;
    private float firstMinSeparation;
    private float firstDistance;
    private T firstRelativePosition;
    private T firstRelativeVelocity;
    private T relativePosition;
    private T relativeVelocity;

    public CollisionAvoidance(Steerable<T> owner, Proximity<T> proximity) {
        super(owner, proximity);
        this.firstRelativePosition = this.newVector(owner);
        this.firstRelativeVelocity = this.newVector(owner);
        this.relativeVelocity = this.newVector(owner);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        this.shortestTime = Float.POSITIVE_INFINITY;
        this.firstNeighbor = null;
        this.firstMinSeparation = 0.0f;
        this.firstDistance = 0.0f;
        this.relativePosition = steering.linear;
        int neighborCount = this.proximity.findNeighbors(this);
        if (neighborCount == 0 || this.firstNeighbor == null) {
            return steering.setZero();
        }
        if (this.firstMinSeparation <= 0.0f || this.firstDistance < this.owner.getBoundingRadius() + this.firstNeighbor.getBoundingRadius()) {
            this.relativePosition.set(this.firstNeighbor.getPosition()).sub(this.owner.getPosition());
        } else {
            this.relativePosition.set(this.firstRelativePosition).mulAdd(this.firstRelativeVelocity, (float)this.shortestTime);
        }
        this.relativePosition.nor().scl((float)(- this.getActualLimiter().getMaxLinearAcceleration()));
        steering.angular = 0.0f;
        return steering;
    }

    @Override
    public boolean reportNeighbor(Steerable<T> neighbor) {
        this.relativePosition.set(neighbor.getPosition()).sub(this.owner.getPosition());
        this.relativeVelocity.set(neighbor.getLinearVelocity()).sub(this.owner.getLinearVelocity());
        float relativeSpeed2 = this.relativeVelocity.len2();
        if (relativeSpeed2 == 0.0f) {
            return false;
        }
        float timeToCollision = (- this.relativePosition.dot(this.relativeVelocity)) / relativeSpeed2;
        if (timeToCollision <= 0.0f || timeToCollision >= this.shortestTime) {
            return false;
        }
        float distance = this.relativePosition.len();
        float minSeparation = distance - (float)Math.sqrt(relativeSpeed2) * timeToCollision;
        if (minSeparation > this.owner.getBoundingRadius() + neighbor.getBoundingRadius()) {
            return false;
        }
        this.shortestTime = timeToCollision;
        this.firstNeighbor = neighbor;
        this.firstMinSeparation = minSeparation;
        this.firstDistance = distance;
        this.firstRelativePosition.set(this.relativePosition);
        this.firstRelativeVelocity.set(this.relativeVelocity);
        return true;
    }

    @Override
    public CollisionAvoidance<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public CollisionAvoidance<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public CollisionAvoidance<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

