/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.BoundedSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BooleanArray;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SoftRoleSlotAssignmentStrategy<T extends Vector<T>>
extends BoundedSlotAssignmentStrategy<T> {
    protected SlotCostProvider<T> slotCostProvider;
    protected float costThreshold;
    private BooleanArray filledSlots;

    public SoftRoleSlotAssignmentStrategy(SlotCostProvider<T> slotCostProvider) {
        this(slotCostProvider, Float.POSITIVE_INFINITY);
    }

    public SoftRoleSlotAssignmentStrategy(SlotCostProvider<T> slotCostProvider, float costThreshold) {
        this.slotCostProvider = slotCostProvider;
        this.costThreshold = costThreshold;
        this.filledSlots = new BooleanArray();
    }

    @Override
    public void updateSlotAssignments(Array<SlotAssignment<T>> assignments) {
        SlotAssignment<T> slot;
        int i;
        int j;
        Array memberData = new Array();
        int numberOfAssignments = assignments.size;
        for (i = 0; i < numberOfAssignments; ++i) {
            SlotAssignment<T> assignment = assignments.get(i);
            MemberAndSlots datum = new MemberAndSlots(assignment.member);
            for (j = 0; j < numberOfAssignments; ++j) {
                float cost = this.slotCostProvider.getCost(assignment.member, j);
                if (cost >= this.costThreshold) continue;
                slot = assignments.get(j);
                CostAndSlot slotDatum = new CostAndSlot(cost, slot.slotNumber);
                datum.costAndSlots.add(slotDatum);
                datum.assignmentEase += 1.0f / (1.0f + cost);
            }
            memberData.add(datum);
        }
        if (numberOfAssignments > this.filledSlots.size) {
            this.filledSlots.ensureCapacity(numberOfAssignments - this.filledSlots.size);
        }
        this.filledSlots.size = numberOfAssignments;
        for (i = 0; i < numberOfAssignments; ++i) {
            this.filledSlots.set(i, false);
        }
        memberData.sort();
        for (i = 0; i < memberData.size; ++i) {
            int slotNumber;
            block7 : {
                MemberAndSlots memberDatum = (MemberAndSlots)memberData.get(i);
                memberDatum.costAndSlots.sort();
                int m = memberDatum.costAndSlots.size;
                for (j = 0; j < m; ++j) {
                    slotNumber = memberDatum.costAndSlots.get((int)j).slotNumber;
                    if (this.filledSlots.get(slotNumber)) {
                        continue;
                    }
                    break block7;
                }
                throw new GdxRuntimeException("SoftRoleSlotAssignmentStrategy cannot find valid slot assignment for member " + memberDatum.member);
            }
            slot = assignments.get(slotNumber);
            slot.member = memberDatum.member;
            slot.slotNumber = slotNumber;
            this.filledSlots.set(slotNumber, true);
        }
    }

    public static interface SlotCostProvider<T extends Vector<T>> {
        public float getCost(FormationMember<T> var1, int var2);
    }

    static class MemberAndSlots<T extends Vector<T>>
    implements Comparable<MemberAndSlots<T>> {
        FormationMember<T> member;
        float assignmentEase;
        Array<CostAndSlot<T>> costAndSlots;

        public MemberAndSlots(FormationMember<T> member) {
            this.member = member;
            this.assignmentEase = 0.0f;
            this.costAndSlots = new Array();
        }

        @Override
        public int compareTo(MemberAndSlots<T> other) {
            return this.assignmentEase < other.assignmentEase ? -1 : (this.assignmentEase > other.assignmentEase ? 1 : 0);
        }
    }

    static class CostAndSlot<T extends Vector<T>>
    implements Comparable<CostAndSlot<T>> {
        float cost;
        int slotNumber;

        public CostAndSlot(float cost, int slotNumber) {
            this.cost = cost;
            this.slotNumber = slotNumber;
        }

        @Override
        public int compareTo(CostAndSlot<T> other) {
            return this.cost < other.cost ? -1 : (this.cost > other.cost ? 1 : 0);
        }
    }

}

