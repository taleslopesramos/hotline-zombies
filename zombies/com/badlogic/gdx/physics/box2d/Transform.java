/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

public class Transform {
    public static final int POS_X = 0;
    public static final int POS_Y = 1;
    public static final int COS = 2;
    public static final int SIN = 3;
    public float[] vals = new float[4];
    private Vector2 position = new Vector2();
    private Vector2 orientation = new Vector2();

    public Transform() {
    }

    public Transform(Vector2 position, float angle) {
        this.setPosition(position);
        this.setRotation(angle);
    }

    public Transform(Vector2 position, Vector2 orientation) {
        this.setPosition(position);
        this.setOrientation(orientation);
    }

    public Vector2 mul(Vector2 v) {
        float x = this.vals[0] + this.vals[2] * v.x + (- this.vals[3]) * v.y;
        float y = this.vals[1] + this.vals[3] * v.x + this.vals[2] * v.y;
        v.x = x;
        v.y = y;
        return v;
    }

    public Vector2 getPosition() {
        return this.position.set(this.vals[0], this.vals[1]);
    }

    public void setRotation(float angle) {
        float c = (float)Math.cos(angle);
        float s = (float)Math.sin(angle);
        this.vals[2] = c;
        this.vals[3] = s;
    }

    public float getRotation() {
        return (float)Math.atan2(this.vals[3], this.vals[2]);
    }

    public Vector2 getOrientation() {
        return this.orientation.set(this.vals[2], this.vals[3]);
    }

    public void setOrientation(Vector2 orientation) {
        this.vals[2] = orientation.x;
        this.vals[3] = orientation.y;
    }

    public void setPosition(Vector2 pos) {
        this.vals[0] = pos.x;
        this.vals[1] = pos.y;
    }
}

