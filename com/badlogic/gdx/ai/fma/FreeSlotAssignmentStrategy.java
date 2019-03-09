/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.ai.fma.SlotAssignmentStrategy;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public class FreeSlotAssignmentStrategy<T extends Vector<T>>
implements SlotAssignmentStrategy<T> {
    @Override
    public void updateSlotAssignments(Array<SlotAssignment<T>> assignments) {
        int i = 0;
        while (i < assignments.size) {
            assignments.get((int)i).slotNumber = i++;
        }
    }

    @Override
    public int calculateNumberOfSlots(Array<SlotAssignment<T>> assignments) {
        return assignments.size;
    }

    @Override
    public void removeSlotAssignment(Array<SlotAssignment<T>> assignments, int index) {
        assignments.removeIndex(index);
    }
}

