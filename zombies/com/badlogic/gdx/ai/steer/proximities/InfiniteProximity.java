/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.proximities;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.ProximityBase;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public class InfiniteProximity<T extends Vector<T>>
extends ProximityBase<T> {
    public InfiniteProximity(Steerable<T> owner, Array<? extends Steerable<T>> agents) {
        super(owner, agents);
    }

    @Override
    public int findNeighbors(Proximity.ProximityCallback<T> callback) {
        int neighborCount = 0;
        int agentCount = this.agents.size;
        for (int i = 0; i < agentCount; ++i) {
            Steerable currentAgent = (Steerable)this.agents.get(i);
            if (currentAgent == this.owner || !callback.reportNeighbor(currentAgent)) continue;
            ++neighborCount;
        }
        return neighborCount;
    }
}

