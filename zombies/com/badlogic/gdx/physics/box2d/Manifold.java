/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

public class Manifold {
    long addr;
    final ManifoldPoint[] points;
    final Vector2 localNormal;
    final Vector2 localPoint;
    final int[] tmpInt;
    final float[] tmpFloat;

    protected Manifold(long addr) {
        this.points = new ManifoldPoint[]{new ManifoldPoint(), new ManifoldPoint()};
        this.localNormal = new Vector2();
        this.localPoint = new Vector2();
        this.tmpInt = new int[2];
        this.tmpFloat = new float[4];
        this.addr = addr;
    }

    public ManifoldType getType() {
        int type = this.jniGetType(this.addr);
        if (type == 0) {
            return ManifoldType.Circle;
        }
        if (type == 1) {
            return ManifoldType.FaceA;
        }
        if (type == 2) {
            return ManifoldType.FaceB;
        }
        return ManifoldType.Circle;
    }

    private native int jniGetType(long var1);

    public int getPointCount() {
        return this.jniGetPointCount(this.addr);
    }

    private native int jniGetPointCount(long var1);

    public Vector2 getLocalNormal() {
        this.jniGetLocalNormal(this.addr, this.tmpFloat);
        this.localNormal.set(this.tmpFloat[0], this.tmpFloat[1]);
        return this.localNormal;
    }

    private native void jniGetLocalNormal(long var1, float[] var3);

    public Vector2 getLocalPoint() {
        this.jniGetLocalPoint(this.addr, this.tmpFloat);
        this.localPoint.set(this.tmpFloat[0], this.tmpFloat[1]);
        return this.localPoint;
    }

    private native void jniGetLocalPoint(long var1, float[] var3);

    public ManifoldPoint[] getPoints() {
        int count = this.jniGetPointCount(this.addr);
        for (int i = 0; i < count; ++i) {
            int contactID = this.jniGetPoint(this.addr, this.tmpFloat, i);
            ManifoldPoint point = this.points[i];
            point.contactID = contactID;
            point.localPoint.set(this.tmpFloat[0], this.tmpFloat[1]);
            point.normalImpulse = this.tmpFloat[2];
            point.tangentImpulse = this.tmpFloat[3];
        }
        return this.points;
    }

    private native int jniGetPoint(long var1, float[] var3, int var4);

    public static enum ManifoldType {
        Circle,
        FaceA,
        FaceB;
        

        private ManifoldType() {
        }
    }

    public class ManifoldPoint {
        public final Vector2 localPoint;
        public float normalImpulse;
        public float tangentImpulse;
        public int contactID;

        public ManifoldPoint() {
            this.localPoint = new Vector2();
            this.contactID = 0;
        }

        public String toString() {
            return "id: " + this.contactID + ", " + this.localPoint + ", " + this.normalImpulse + ", " + this.tangentImpulse;
        }
    }

}

