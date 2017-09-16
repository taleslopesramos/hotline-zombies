/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.proximities;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.ProximityBase;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public class RadiusProximity<T extends Vector<T>>
extends ProximityBase<T> {
    protected float radius;
    private float lastTime;

    public RadiusProximity(Steerable<T> owner, Array<? extends Steerable<T>> agents, float radius) {
        super(owner, agents);
        this.radius = radius;
        this.lastTime = 0.0f;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public int findNeighbors(Proximity.ProximityCallback<T> callback) {
        int agentCount = this.agents.size;
        int neighborCount = 0;
        float currentTime = GdxAI.getTimepiece().getTime();
        if (this.lastTime != currentTime) {
            this.lastTime = currentTime;
            Object ownerPosition = this.owner.getPosition();
            for (int i = 0; i < agentCount; ++i) {
                float squareDistance;
                float range;
                Steerable currentAgent = (Steerable)this.agents.get(i);
                if (currentAgent != this.owner && (squareDistance = ownerPosition.dst2(currentAgent.getPosition())) < (range = this.radius + currentAgent.getBoundingRadius()) * range && callback.reportNeighbor(currentAgent)) {
                    currentAgent.setTagged(true);
                    ++neighborCount;
                    continue;
                }
                currentAgent.setTagged(false);
            }
        } else {
            for (int i = 0; i < agentCount; ++i) {
                Steerable currentAgent = (Steerable)this.agents.get(i);
                if (currentAgent == this.owner || !currentAgent.isTagged() || !callback.reportNeighbor(currentAgent)) continue;
                ++neighborCount;
            }
        }
        return neighborCount;
    }
}

