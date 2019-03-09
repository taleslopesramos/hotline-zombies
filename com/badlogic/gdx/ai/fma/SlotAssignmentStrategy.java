/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public interface SlotAssignmentStrategy<T extends Vector<T>> {
    public void updateSlotAssignments(Array<SlotAssignment<T>> var1);

    public int calculateNumberOfSlots(Array<SlotAssignment<T>> var1);

    public void removeSlotAssignment(Array<SlotAssignment<T>> var1, int var2);
}

