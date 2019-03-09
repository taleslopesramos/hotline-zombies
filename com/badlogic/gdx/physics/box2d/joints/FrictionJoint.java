/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

public class FrictionJoint
extends Joint {
    private final float[] tmp = new float[2];
    private final Vector2 localAnchorA = new Vector2();
    private final Vector2 localAnchorB = new Vector2();

    public FrictionJoint(World world, long addr) {
        super(world, addr);
    }

    public Vector2 getLocalAnchorA() {
        this.jniGetLocalAnchorA(this.addr, this.tmp);
        this.localAnchorA.set(this.tmp[0], this.tmp[1]);
        return this.localAnchorA;
    }

    private native void jniGetLocalAnchorA(long var1, float[] var3);

    public Vector2 getLocalAnchorB() {
        this.jniGetLocalAnchorB(this.addr, this.tmp);
        this.localAnchorB.set(this.tmp[0], this.tmp[1]);
        return this.localAnchorB;
    }

    private native void jniGetLocalAnchorB(long var1, float[] var3);

    public void setMaxForce(float force) {
        this.jniSetMaxForce(this.addr, force);
    }

    private native void jniSetMaxForce(long var1, float var3);

    public float getMaxForce() {
        return this.jniGetMaxForce(this.addr);
    }

    private native float jniGetMaxForce(long var1);

    public void setMaxTorque(float torque) {
        this.jniSetMaxTorque(this.addr, torque);
    }

    private native void jniSetMaxTorque(long var1, float var3);

    public float getMaxTorque() {
        return this.jniGetMaxTorque(this.addr);
    }

    private native float jniGetMaxTorque(long var1);
}

