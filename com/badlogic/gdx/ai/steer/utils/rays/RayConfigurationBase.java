/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils.rays;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public abstract class RayConfigurationBase<T extends Vector<T>>
implements RayConfiguration<T> {
    protected Steerable<T> owner;
    protected Ray<T>[] rays;

    public RayConfigurationBase(Steerable<T> owner, int numRays) {
        this.owner = owner;
        this.rays = new Ray[numRays];
        for (int i = 0; i < numRays; ++i) {
            this.rays[i] = new Ray(owner.getPosition().cpy().setZero(), owner.getPosition().cpy().setZero());
        }
    }

    public Steerable<T> getOwner() {
        return this.owner;
    }

    public void setOwner(Steerable<T> owner) {
        this.owner = owner;
    }

    public Ray<T>[] getRays() {
        return this.rays;
    }

    public void setRays(Ray<T>[] rays) {
        this.rays = rays;
    }
}

