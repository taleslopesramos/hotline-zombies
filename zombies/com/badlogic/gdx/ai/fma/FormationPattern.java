/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public interface FormationPattern<T extends Vector<T>> {
    public void setNumberOfSlots(int var1);

    public Location<T> calculateSlotLocation(Location<T> var1, int var2);

    public boolean supportsSlots(int var1);
}

