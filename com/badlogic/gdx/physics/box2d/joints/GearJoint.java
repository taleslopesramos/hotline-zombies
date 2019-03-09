/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

public class GearJoint
extends Joint {
    private Joint joint1;
    private Joint joint2;

    public GearJoint(World world, long addr, Joint joint1, Joint joint2) {
        super(world, addr);
        this.joint1 = joint1;
        this.joint2 = joint2;
    }

    public Joint getJoint1() {
        return this.joint1;
    }

    private native long jniGetJoint1(long var1);

    public Joint getJoint2() {
        return this.joint2;
    }

    private native long jniGetJoint2(long var1);

    public void setRatio(float ratio) {
        this.jniSetRatio(this.addr, ratio);
    }

    private native void jniSetRatio(long var1, float var3);

    public float getRatio() {
        return this.jniGetRatio(this.addr);
    }

    private native float jniGetRatio(long var1);
}

