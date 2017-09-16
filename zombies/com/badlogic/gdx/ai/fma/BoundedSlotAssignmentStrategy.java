/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.ai.fma.SlotAssignmentStrategy;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public abstract class BoundedSlotAssignmentStrategy<T extends Vector<T>>
implements SlotAssignmentStrategy<T> {
    @Override
    public abstract void updateSlotAssignments(Array<SlotAssignment<T>> var1);

    @Override
    public int calculateNumberOfSlots(Array<SlotAssignment<T>> assignments) {
        int filledSlots = -1;
        for (int i = 0; i < assignments.size; ++i) {
            SlotAssignment<T> assignment = assignments.get(i);
            if (assignment.slotNumber < filledSlots) continue;
            filledSlots = assignment.slotNumber;
        }
        return filledSlots + 1;
    }

    @Override
    public void removeSlotAssignment(Array<SlotAssignment<T>> assignments, int index) {
        int sn = assignments.get((int)index).slotNumber;
        for (int i = 0; i < assignments.size; ++i) {
            SlotAssignment<T> sa = assignments.get(i);
            if (sa.slotNumber < sn) continue;
            --sa.slotNumber;
        }
        assignments.removeIndex(index);
    }
}

