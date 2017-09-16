/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma.patterns;

import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class OffensiveCircleFormationPattern<T extends Vector<T>>
extends DefensiveCircleFormationPattern<T> {
    public OffensiveCircleFormationPattern(float memberRadius) {
        super(memberRadius);
    }

    @Override
    public Location<T> calculateSlotLocation(Location<T> outLocation, int slotNumber) {
        super.calculateSlotLocation(outLocation, slotNumber);
        outLocation.setOrientation(outLocation.getOrientation() + 3.1415927f);
        return outLocation;
    }
}

