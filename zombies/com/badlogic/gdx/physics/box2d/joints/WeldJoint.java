/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

public class WeldJoint
extends Joint {
    private final float[] tmp = new float[2];
    private final Vector2 localAnchorA = new Vector2();
    private final Vector2 localAnchorB = new Vector2();

    public WeldJoint(World world, long addr) {
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

    public float getFrequency() {
        return this.jniGetFrequency(this.addr);
    }

    private native float jniGetFrequency(long var1);

    public void setFrequency(float hz) {
        this.jniSetFrequency(this.addr, hz);
    }

    private native void jniSetFrequency(long var1, float var3);

    public float getDampingRatio() {
        return this.jniGetDampingRatio(this.addr);
    }

    private native float jniGetDampingRatio(long var1);

    public void setDampingRatio(float ratio) {
        this.jniSetDampingRatio(this.addr, ratio);
    }

    private native void jniSetDampingRatio(long var1, float var3);
}

