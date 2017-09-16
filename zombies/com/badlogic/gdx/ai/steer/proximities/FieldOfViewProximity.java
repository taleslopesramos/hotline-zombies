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

public class FieldOfViewProximity<T extends Vector<T>>
extends ProximityBase<T> {
    protected float radius;
    protected float angle;
    private float coneThreshold;
    private float lastTime;
    private T ownerOrientation;
    private T toAgent;

    public FieldOfViewProximity(Steerable<T> owner, Array<? extends Steerable<T>> agents, float radius, float angle) {
        super(owner, agents);
        this.radius = radius;
        this.setAngle(angle);
        this.lastTime = 0.0f;
        this.ownerOrientation = owner.getPosition().cpy().setZero();
        this.toAgent = owner.getPosition().cpy().setZero();
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.coneThreshold = (float)Math.cos(angle * 0.5f);
    }

    @Override
    public int findNeighbors(Proximity.ProximityCallback<T> callback) {
        int neighborCount = 0;
        int agentCount = this.agents.size;
        float currentTime = GdxAI.getTimepiece().getTime();
        if (this.lastTime != currentTime) {
            this.lastTime = currentTime;
            Object ownerPosition = this.owner.getPosition();
            this.owner.angleToVector(this.ownerOrientation, this.owner.getOrientation());
            for (int i = 0; i < agentCount; ++i) {
                Steerable currentAgent = (Steerable)this.agents.get(i);
                if (currentAgent != this.owner) {
                    this.toAgent.set(currentAgent.getPosition()).sub(ownerPosition);
                    float range = this.radius + currentAgent.getBoundingRadius();
                    float toAgentLen2 = this.toAgent.len2();
                    if (toAgentLen2 < range * range && this.ownerOrientation.dot(this.toAgent) > this.coneThreshold && callback.reportNeighbor(currentAgent)) {
                        currentAgent.setTagged(true);
                        ++neighborCount;
                        continue;
                    }
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

