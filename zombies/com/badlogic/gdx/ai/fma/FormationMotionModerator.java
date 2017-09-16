/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public abstract class FormationMotionModerator<T extends Vector<T>> {
    private Location<T> tempLocation;

    public abstract void updateAnchorPoint(Location<T> var1);

    public Location<T> calculateDriftOffset(Location<T> centerOfMass, Array<SlotAssignment<T>> slotAssignments, FormationPattern<T> pattern) {
        centerOfMass.getPosition().setZero();
        float centerOfMassOrientation = 0.0f;
        if (this.tempLocation == null) {
            this.tempLocation = centerOfMass.newLocation();
        }
        T centerOfMassPos = centerOfMass.getPosition();
        T tempLocationPos = this.tempLocation.getPosition();
        float numberOfAssignments = slotAssignments.size;
        int i = 0;
        while ((float)i < numberOfAssignments) {
            pattern.calculateSlotLocation(this.tempLocation, slotAssignments.get((int)i).slotNumber);
            centerOfMassPos.add(tempLocationPos);
            centerOfMassOrientation += this.tempLocation.getOrientation();
            ++i;
        }
        centerOfMassPos.scl((float)(1.0f / numberOfAssignments));
        centerOfMass.setOrientation(centerOfMassOrientation /= numberOfAssignments);
        return centerOfMass;
    }
}

