/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;

public abstract class GroupBehavior<T extends Vector<T>>
extends SteeringBehavior<T> {
    protected Proximity<T> proximity;

    public GroupBehavior(Steerable<T> owner, Proximity<T> proximity) {
        super(owner);
        this.proximity = proximity;
    }

    public Proximity<T> getProximity() {
        return this.proximity;
    }

    public void setProximity(Proximity<T> proximity) {
        this.proximity = proximity;
    }
}

