/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils;

import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public interface RayConfiguration<T extends Vector<T>> {
    public Ray<T>[] updateRays();
}

