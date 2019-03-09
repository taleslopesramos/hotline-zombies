/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma.patterns;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class DefensiveCircleFormationPattern<T extends Vector<T>>
implements FormationPattern<T> {
    int numberOfSlots;
    float memberRadius;

    public DefensiveCircleFormationPattern(float memberRadius) {
        this.memberRadius = memberRadius;
    }

    @Override
    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    @Override
    public Location<T> calculateSlotLocation(Location<T> outLocation, int slotNumber) {
        if (this.numberOfSlots > 1) {
            float angleAroundCircle = 6.2831855f * (float)slotNumber / (float)this.numberOfSlots;
            float radius = this.memberRadius / (float)Math.sin(3.141592653589793 / (double)this.numberOfSlots);
            outLocation.angleToVector(outLocation.getPosition(), angleAroundCircle).scl((float)radius);
            outLocation.setOrientation(angleAroundCircle);
        } else {
            outLocation.getPosition().setZero();
            outLocation.setOrientation(6.2831855f * (float)slotNumber);
        }
        return outLocation;
    }

    @Override
    public boolean supportsSlots(int slotCount) {
        return true;
    }
}

