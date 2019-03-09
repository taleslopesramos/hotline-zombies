/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;

public class RaycastObstacleAvoidance<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected RayConfiguration<T> rayConfiguration;
    protected RaycastCollisionDetector<T> raycastCollisionDetector;
    protected float distanceFromBoundary;
    private Collision<T> outputCollision;
    private Collision<T> minOutputCollision;

    public RaycastObstacleAvoidance(Steerable<T> owner) {
        this(owner, null);
    }

    public RaycastObstacleAvoidance(Steerable<T> owner, RayConfiguration<T> rayConfiguration) {
        this(owner, rayConfiguration, null);
    }

    public RaycastObstacleAvoidance(Steerable<T> owner, RayConfiguration<T> rayConfiguration, RaycastCollisionDetector<T> raycastCollisionDetector) {
        this(owner, rayConfiguration, raycastCollisionDetector, 0.0f);
    }

    public RaycastObstacleAvoidance(Steerable<T> owner, RayConfiguration<T> rayConfiguration, RaycastCollisionDetector<T> raycastCollisionDetector, float distanceFromBoundary) {
        super(owner);
        this.rayConfiguration = rayConfiguration;
        this.raycastCollisionDetector = raycastCollisionDetector;
        this.distanceFromBoundary = distanceFromBoundary;
        this.outputCollision = new Collision<T>(this.newVector(owner), this.newVector(owner));
        this.minOutputCollision = new Collision<T>(this.newVector(owner), this.newVector(owner));
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        Object ownerPosition = this.owner.getPosition();
        float minDistanceSquare = Float.POSITIVE_INFINITY;
        Ray<T>[] inputRays = this.rayConfiguration.updateRays();
        for (int i = 0; i < inputRays.length; ++i) {
            float distanceSquare;
            boolean collided = this.raycastCollisionDetector.findCollision(this.outputCollision, inputRays[i]);
            if (!collided || (distanceSquare = ownerPosition.dst2(this.outputCollision.point)) >= minDistanceSquare) continue;
            minDistanceSquare = distanceSquare;
            Collision<T> tmpCollision = this.outputCollision;
            this.outputCollision = this.minOutputCollision;
            this.minOutputCollision = tmpCollision;
        }
        if (minDistanceSquare == Float.POSITIVE_INFINITY) {
            return steering.setZero();
        }
        steering.linear.set(this.minOutputCollision.point).mulAdd(this.minOutputCollision.normal, (float)(this.owner.getBoundingRadius() + this.distanceFromBoundary)).sub(this.owner.getPosition()).nor().scl((float)this.getActualLimiter().getMaxLinearAcceleration());
        steering.angular = 0.0f;
        return steering;
    }

    public RayConfiguration<T> getRayConfiguration() {
        return this.rayConfiguration;
    }

    public RaycastObstacleAvoidance<T> setRayConfiguration(RayConfiguration<T> rayConfiguration) {
        this.rayConfiguration = rayConfiguration;
        return this;
    }

    public RaycastCollisionDetector<T> getRaycastCollisionDetector() {
        return this.raycastCollisionDetector;
    }

    public RaycastObstacleAvoidance<T> setRaycastCollisionDetector(RaycastCollisionDetector<T> raycastCollisionDetector) {
        this.raycastCollisionDetector = raycastCollisionDetector;
        return this;
    }

    public float getDistanceFromBoundary() {
        return this.distanceFromBoundary;
    }

    public RaycastObstacleAvoidance<T> setDistanceFromBoundary(float distanceFromBoundary) {
        this.distanceFromBoundary = distanceFromBoundary;
        return this;
    }

    @Override
    public RaycastObstacleAvoidance<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public RaycastObstacleAvoidance<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public RaycastObstacleAvoidance<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

