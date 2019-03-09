/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Body;

public class JointDef {
    public JointType type = JointType.Unknown;
    public Body bodyA = null;
    public Body bodyB = null;
    public boolean collideConnected = false;

    public static enum JointType {
        Unknown(0),
        RevoluteJoint(1),
        PrismaticJoint(2),
        DistanceJoint(3),
        PulleyJoint(4),
        MouseJoint(5),
        GearJoint(6),
        WheelJoint(7),
        WeldJoint(8),
        FrictionJoint(9),
        RopeJoint(10),
        MotorJoint(11);
        
        public static JointType[] valueTypes;
        private int value;

        private JointType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        static {
            valueTypes = new JointType[]{Unknown, RevoluteJoint, PrismaticJoint, DistanceJoint, PulleyJoint, MouseJoint, GearJoint, WheelJoint, WeldJoint, FrictionJoint, RopeJoint, MotorJoint};
        }
    }

}

