/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.managers;

import com.badlogic.gdx.math.Vector2;

class Room {
    public float xMax;
    public float xMin;
    public float yMax;
    public float yMin;
    public Vector2 doorPos;

    public Room(float xMin, float xMax, float yMin, float yMax, Vector2 doorPos) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.doorPos = doorPos;
    }
}

