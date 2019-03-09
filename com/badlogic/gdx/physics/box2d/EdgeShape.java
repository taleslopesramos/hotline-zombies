/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

public class EdgeShape
extends Shape {
    static final float[] vertex = new float[2];

    public EdgeShape() {
        this.addr = this.newEdgeShape();
    }

    private native long newEdgeShape();

    EdgeShape(long addr) {
        this.addr = addr;
    }

    public void set(Vector2 v1, Vector2 v2) {
        this.set(v1.x, v1.y, v2.x, v2.y);
    }

    public void set(float v1X, float v1Y, float v2X, float v2Y) {
        this.jniSet(this.addr, v1X, v1Y, v2X, v2Y);
    }

    private native void jniSet(long var1, float var3, float var4, float var5, float var6);

    public void getVertex1(Vector2 vec) {
        this.jniGetVertex1(this.addr, vertex);
        vec.x = vertex[0];
        vec.y = vertex[1];
    }

    private native void jniGetVertex1(long var1, float[] var3);

    public void getVertex2(Vector2 vec) {
        this.jniGetVertex2(this.addr, vertex);
        vec.x = vertex[0];
        vec.y = vertex[1];
    }

    private native void jniGetVertex2(long var1, float[] var3);

    public void getVertex0(Vector2 vec) {
        this.jniGetVertex0(this.addr, vertex);
        vec.x = vertex[0];
        vec.y = vertex[1];
    }

    private native void jniGetVertex0(long var1, float[] var3);

    public void setVertex0(Vector2 vec) {
        this.jniSetVertex0(this.addr, vec.x, vec.y);
    }

    public void setVertex0(float x, float y) {
        this.jniSetVertex0(this.addr, x, y);
    }

    private native void jniSetVertex0(long var1, float var3, float var4);

    public void getVertex3(Vector2 vec) {
        this.jniGetVertex3(this.addr, vertex);
        vec.x = vertex[0];
        vec.y = vertex[1];
    }

    private native void jniGetVertex3(long var1, float[] var3);

    public void setVertex3(Vector2 vec) {
        this.jniSetVertex3(this.addr, vec.x, vec.y);
    }

    public void setVertex3(float x, float y) {
        this.jniSetVertex3(this.addr, x, y);
    }

    private native void jniSetVertex3(long var1, float var3, float var4);

    public boolean hasVertex0() {
        return this.jniHasVertex0(this.addr);
    }

    private native boolean jniHasVertex0(long var1);

    public void setHasVertex0(boolean hasVertex0) {
        this.jniSetHasVertex0(this.addr, hasVertex0);
    }

    private native void jniSetHasVertex0(long var1, boolean var3);

    public boolean hasVertex3() {
        return this.jniHasVertex3(this.addr);
    }

    private native boolean jniHasVertex3(long var1);

    public void setHasVertex3(boolean hasVertex3) {
        this.jniSetHasVertex3(this.addr, hasVertex3);
    }

    private native void jniSetHasVertex3(long var1, boolean var3);

    @Override
    public Shape.Type getType() {
        return Shape.Type.Edge;
    }
}

