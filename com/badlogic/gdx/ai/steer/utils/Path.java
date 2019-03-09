/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils;

import com.badlogic.gdx.math.Vector;

public interface Path<T extends Vector<T>, P extends PathParam> {
    public P createParam();

    public boolean isOpen();

    public float getLength();

    public T getStartPoint();

    public T getEndPoint();

    public float calculateDistance(T var1, P var2);

    public void calculateTargetPosition(T var1, P var2, float var3);

    public static interface PathParam {
        public float getDistance();

        public void setDistance(float var1);
    }

}

