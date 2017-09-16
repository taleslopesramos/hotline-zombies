/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector;

public interface Proximity<T extends Vector<T>> {
    public Steerable<T> getOwner();

    public void setOwner(Steerable<T> var1);

    public int findNeighbors(ProximityCallback<T> var1);

    public static interface ProximityCallback<T extends Vector<T>> {
        public boolean reportNeighbor(Steerable<T> var1);
    }

}

