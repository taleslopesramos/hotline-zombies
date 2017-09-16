/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils.rays;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public class CentralRayWithWhiskersConfiguration<T extends Vector<T>>
extends RayConfigurationBase<T> {
    private float rayLength;
    private float whiskerLength;
    private float whiskerAngle;

    public CentralRayWithWhiskersConfiguration(Steerable<T> owner, float rayLength, float whiskerLength, float whiskerAngle) {
        super(owner, 3);
        this.rayLength = rayLength;
        this.whiskerLength = whiskerLength;
        this.whiskerAngle = whiskerAngle;
    }

    @Override
    public Ray<T>[] updateRays() {
        Object ownerPosition = this.owner.getPosition();
        Object ownerVelocity = this.owner.getLinearVelocity();
        float velocityAngle = this.owner.vectorToAngle(ownerVelocity);
        this.rays[0].start.set(ownerPosition);
        this.rays[0].end.set(ownerVelocity).nor().scl((float)this.rayLength).add(ownerPosition);
        this.rays[1].start.set(ownerPosition);
        this.owner.angleToVector(this.rays[1].end, velocityAngle - this.whiskerAngle).scl((float)this.whiskerLength).add(ownerPosition);
        this.rays[2].start.set(ownerPosition);
        this.owner.angleToVector(this.rays[2].end, velocityAngle + this.whiskerAngle).scl((float)this.whiskerLength).add(ownerPosition);
        return this.rays;
    }

    public float getRayLength() {
        return this.rayLength;
    }

    public void setRayLength(float rayLength) {
        this.rayLength = rayLength;
    }

    public float getWhiskerLength() {
        return this.whiskerLength;
    }

    public void setWhiskerLength(float whiskerLength) {
        this.whiskerLength = whiskerLength;
    }

    public float getWhiskerAngle() {
        return this.whiskerAngle;
    }

    public void setWhiskerAngle(float whiskerAngle) {
        this.whiskerAngle = whiskerAngle;
    }
}

