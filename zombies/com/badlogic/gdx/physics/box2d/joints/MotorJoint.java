/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

public class MotorJoint
extends Joint {
    private final float[] tmp = new float[2];
    private final Vector2 linearOffset = new Vector2();

    public MotorJoint(World world, long addr) {
        super(world, addr);
    }

    public Vector2 getLinearOffset() {
        this.jniGetLinearOffset(this.addr, this.tmp);
        this.linearOffset.set(this.tmp[0], this.tmp[1]);
        return this.linearOffset;
    }

    private native void jniGetLinearOffset(long var1, float[] var3);

    public void setLinearOffset(Vector2 linearOffset) {
        this.jniSetLinearOffset(this.addr, linearOffset.x, linearOffset.y);
    }

    private native void jniSetLinearOffset(long var1, float var3, float var4);

    public float getAngularOffset() {
        return this.jniGetAngularOffset(this.addr);
    }

    private native float jniGetAngularOffset(long var1);

    public void setAngularOffset(float angularOffset) {
        this.jniSetAngularOffset(this.addr, angularOffset);
    }

    private native void jniSetAngularOffset(long var1, float var3);

    public float getMaxForce() {
        return this.jniGetMaxForce(this.addr);
    }

    private native float jniGetMaxForce(long var1);

    public void setMaxForce(float maxForce) {
        this.jniSetMaxForce(this.addr, maxForce);
    }

    private native void jniSetMaxForce(long var1, float var3);

    public float getMaxTorque() {
        return this.jniGetMaxTorque(this.addr);
    }

    private native float jniGetMaxTorque(long var1);

    public void setMaxTorque(float maxTorque) {
        this.jniSetMaxTorque(this.addr, maxTorque);
    }

    private native void jniSetMaxTorque(long var1, float var3);

    public float getCorrectionFactor() {
        return this.jniGetCorrectionFactor(this.addr);
    }

    private native float jniGetCorrectionFactor(long var1);

    public void setCorrectionFactor(float correctionFactor) {
        this.jniSetCorrectionFactor(this.addr, correctionFactor);
    }

    private native void jniSetCorrectionFactor(long var1, float var3);
}

