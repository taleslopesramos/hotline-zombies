/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

public class WorldManifold {
    protected final Vector2 normal = new Vector2();
    protected final Vector2[] points = new Vector2[]{new Vector2(), new Vector2()};
    protected final float[] separations = new float[2];
    protected int numContactPoints;

    protected WorldManifold() {
    }

    public Vector2 getNormal() {
        return this.normal;
    }

    public Vector2[] getPoints() {
        return this.points;
    }

    public float[] getSeparations() {
        return this.separations;
    }

    public int getNumberOfContactPoints() {
        return this.numContactPoints;
    }
}

