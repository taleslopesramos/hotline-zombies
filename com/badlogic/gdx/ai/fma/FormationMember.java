/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public interface FormationMember<T extends Vector<T>> {
    public Location<T> getTargetLocation();
}

