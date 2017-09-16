/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.proximities;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public abstract class ProximityBase<T extends Vector<T>>
implements Proximity<T> {
    protected Steerable<T> owner;
    protected Array<? extends Steerable<T>> agents;

    public ProximityBase(Steerable<T> owner, Array<? extends Steerable<T>> agents) {
        this.owner = owner;
        this.agents = agents;
    }

    @Override
    public Steerable<T> getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(Steerable<T> owner) {
        this.owner = owner;
    }

    public Array<? extends Steerable<T>> getAgents() {
        return this.agents;
    }

    public void setAgents(Array<Steerable<T>> agents) {
        this.agents = agents;
    }
}

