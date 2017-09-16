/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.math.Vector;

public class SlotAssignment<T extends Vector<T>> {
    public FormationMember<T> member;
    public int slotNumber;

    public SlotAssignment(FormationMember<T> member) {
        this(member, 0);
    }

    public SlotAssignment(FormationMember<T> member, int slotNumber) {
        this.member = member;
        this.slotNumber = slotNumber;
    }
}

