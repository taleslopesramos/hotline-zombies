/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public class PrioritySteering<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected float epsilon;
    protected Array<SteeringBehavior<T>> behaviors = new Array();
    protected int selectedBehaviorIndex;

    public PrioritySteering(Steerable<T> owner) {
        this(owner, 0.001f);
    }

    public PrioritySteering(Steerable<T> owner, float epsilon) {
        super(owner);
        this.epsilon = epsilon;
    }

    public PrioritySteering<T> add(SteeringBehavior<T> behavior) {
        this.behaviors.add(behavior);
        return this;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        float epsilonSquared = this.epsilon * this.epsilon;
        int n = this.behaviors.size;
        this.selectedBehaviorIndex = -1;
        for (int i = 0; i < n; ++i) {
            this.selectedBehaviorIndex = i;
            SteeringBehavior<T> behavior = this.behaviors.get(i);
            behavior.calculateSteering(steering);
            if (steering.calculateSquareMagnitude() <= epsilonSquared) continue;
            return steering;
        }
        return n > 0 ? steering : steering.setZero();
    }

    public int getSelectedBehaviorIndex() {
        return this.selectedBehaviorIndex;
    }

    public float getEpsilon() {
        return this.epsilon;
    }

    public PrioritySteering<T> setEpsilon(float epsilon) {
        this.epsilon = epsilon;
        return this;
    }

    @Override
    public PrioritySteering<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public PrioritySteering<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public PrioritySteering<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }
}

