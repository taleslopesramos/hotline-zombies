/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils.rays;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public class SingleRayConfiguration<T extends Vector<T>>
extends RayConfigurationBase<T> {
    private float length;

    public SingleRayConfiguration(Steerable<T> owner, float length) {
        super(owner, 1);
        this.length = length;
    }

    @Override
    public Ray<T>[] updateRays() {
        this.rays[0].start.set(this.owner.getPosition());
        this.rays[0].end.set(this.owner.getLinearVelocity()).nor().scl((float)this.length).add(this.rays[0].start);
        return this.rays;
    }

    public float getLength() {
        return this.length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}

