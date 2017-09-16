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
import com.badlogic.gdx.utils.Array;

public class BlendedSteering<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Array<BehaviorAndWeight<T>> list = new Array();
    private SteeringAcceleration<T> steering;

    public BlendedSteering(Steerable<T> owner) {
        super(owner);
        this.steering = new SteeringAcceleration<T>(this.newVector(owner));
    }

    public BlendedSteering<T> add(SteeringBehavior<T> behavior, float weight) {
        return this.add(new BehaviorAndWeight<T>(behavior, weight));
    }

    public BlendedSteering<T> add(BehaviorAndWeight<T> item) {
        item.behavior.setOwner(this.owner);
        this.list.add(item);
        return this;
    }

    public BehaviorAndWeight<T> get(int index) {
        return this.list.get(index);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> blendedSteering) {
        blendedSteering.setZero();
        int len = this.list.size;
        for (int i = 0; i < len; ++i) {
            BehaviorAndWeight<T> bw = this.list.get(i);
            bw.behavior.calculateSteering(this.steering);
            blendedSteering.mulAdd(this.steering, bw.weight);
        }
        Limiter actualLimiter = this.getActualLimiter();
        blendedSteering.linear.limit(actualLimiter.getMaxLinearAcceleration());
        if (blendedSteering.angular > actualLimiter.getMaxAngularAcceleration()) {
            blendedSteering.angular = actualLimiter.getMaxAngularAcceleration();
        }
        return blendedSteering;
    }

    @Override
    public BlendedSteering<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public BlendedSteering<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public BlendedSteering<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    public static class BehaviorAndWeight<T extends Vector<T>> {
        protected SteeringBehavior<T> behavior;
        protected float weight;

        public BehaviorAndWeight(SteeringBehavior<T> behavior, float weight) {
            this.behavior = behavior;
            this.weight = weight;
        }

        public SteeringBehavior<T> getBehavior() {
            return this.behavior;
        }

        public void setBehavior(SteeringBehavior<T> behavior) {
            this.behavior = behavior;
        }

        public float getWeight() {
            return this.weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }
    }

}

