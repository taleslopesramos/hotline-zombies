/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;

public class JointEdge {
    public final Body other;
    public final Joint joint;

    protected JointEdge(Body other, Joint joint) {
        this.other = other;
        this.joint = joint;
    }
}

