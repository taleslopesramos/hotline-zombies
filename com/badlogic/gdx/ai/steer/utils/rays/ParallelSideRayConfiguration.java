/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils.rays;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public class ParallelSideRayConfiguration<T extends Vector<T>>
extends RayConfigurationBase<T> {
    private static final float HALF_PI = 1.5707964f;
    private float length;
    private float sideOffset;

    public ParallelSideRayConfiguration(Steerable<T> owner, float length, float sideOffset) {
        super(owner, 2);
        this.length = length;
        this.sideOffset = sideOffset;
    }

    @Override
    public Ray<T>[] updateRays() {
        float velocityAngle = this.owner.vectorToAngle(this.owner.getLinearVelocity());
        this.owner.angleToVector(this.rays[0].start, velocityAngle - 1.5707964f).scl((float)this.sideOffset).add(this.owner.getPosition());
        this.rays[0].end.set(this.owner.getLinearVelocity()).nor().scl((float)this.length);
        this.owner.angleToVector(this.rays[1].start, velocityAngle + 1.5707964f).scl((float)this.sideOffset).add(this.owner.getPosition());
        this.rays[1].end.set(this.rays[0].end).add(this.rays[1].start);
        this.rays[0].end.add(this.rays[0].start);
        return this.rays;
    }

    public float getLength() {
        return this.length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getSideOffset() {
        return this.sideOffset;
    }

    public void setSideOffset(float sideOffset) {
        this.sideOffset = sideOffset;
    }
}

