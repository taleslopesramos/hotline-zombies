/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public interface RaycastCollisionDetector<T extends Vector<T>> {
    public boolean collides(Ray<T> var1);

    public boolean findCollision(Collision<T> var1, Ray<T> var2);
}

