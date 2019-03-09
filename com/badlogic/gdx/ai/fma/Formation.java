/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.FormationMotionModerator;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.FreeSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.ai.fma.SlotAssignmentStrategy;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Formation<T extends Vector<T>> {
    Array<SlotAssignment<T>> slotAssignments;
    protected Location<T> anchor;
    protected FormationPattern<T> pattern;
    protected SlotAssignmentStrategy<T> slotAssignmentStrategy;
    protected FormationMotionModerator<T> motionModerator;
    private final T positionOffset;
    private final Matrix3 orientationMatrix = new Matrix3();
    private final Location<T> driftOffset;

    public Formation(Location<T> anchor, FormationPattern<T> pattern) {
        this(anchor, pattern, new FreeSlotAssignmentStrategy(), null);
    }

    public Formation(Location<T> anchor, FormationPattern<T> pattern, SlotAssignmentStrategy<T> slotAssignmentStrategy) {
        this(anchor, pattern, slotAssignmentStrategy, null);
    }

    public Formation(Location<T> anchor, FormationPattern<T> pattern, SlotAssignmentStrategy<T> slotAssignmentStrategy, FormationMotionModerator<T> motionModerator) {
        if (anchor == null) {
            throw new IllegalArgumentException("The anchor point cannot be null");
        }
        this.anchor = anchor;
        this.pattern = pattern;
        this.slotAssignmentStrategy = slotAssignmentStrategy;
        this.motionModerator = motionModerator;
        this.slotAssignments = new Array();
        this.driftOffset = anchor.newLocation();
        this.positionOffset = anchor.getPosition().cpy();
    }

    public Location<T> getAnchorPoint() {
        return this.anchor;
    }

    public void setAnchorPoint(Location<T> anchor) {
        this.anchor = anchor;
    }

    public FormationPattern<T> getPattern() {
        return this.pattern;
    }

    public void setPattern(FormationPattern<T> pattern) {
        this.pattern = pattern;
    }

    public SlotAssignmentStrategy<T> getSlotAssignmentStrategy() {
        return this.slotAssignmentStrategy;
    }

    public void setSlotAssignmentStrategy(SlotAssignmentStrategy<T> slotAssignmentStrategy) {
        this.slotAssignmentStrategy = slotAssignmentStrategy;
    }

    public FormationMotionModerator<T> getMotionModerator() {
        return this.motionModerator;
    }

    public void setMotionModerator(FormationMotionModerator<T> motionModerator) {
        this.motionModerator = motionModerator;
    }

    public void updateSlotAssignments() {
        this.slotAssignmentStrategy.updateSlotAssignments(this.slotAssignments);
        this.pattern.setNumberOfSlots(this.slotAssignmentStrategy.calculateNumberOfSlots(this.slotAssignments));
        if (this.motionModerator != null) {
            this.motionModerator.calculateDriftOffset(this.driftOffset, this.slotAssignments, this.pattern);
        }
    }

    public boolean changePattern(FormationPattern<T> pattern) {
        int occupiedSlots = this.slotAssignments.size;
        if (pattern.supportsSlots(occupiedSlots)) {
            this.setPattern(pattern);
            this.updateSlotAssignments();
            return true;
        }
        return false;
    }

    public boolean addMember(FormationMember<T> member) {
        int occupiedSlots = this.slotAssignments.size;
        if (this.pattern.supportsSlots(occupiedSlots + 1)) {
            this.slotAssignments.add(new SlotAssignment<T>(member, occupiedSlots));
            this.updateSlotAssignments();
            return true;
        }
        return false;
    }

    public void removeMember(FormationMember<T> member) {
        int slot = this.findMemberSlot(member);
        if (slot >= 0) {
            this.slotAssignmentStrategy.removeSlotAssignment(this.slotAssignments, slot);
            this.updateSlotAssignments();
        }
    }

    private int findMemberSlot(FormationMember<T> member) {
        for (int i = 0; i < this.slotAssignments.size; ++i) {
            if (this.slotAssignments.get((int)i).member != member) continue;
            return i;
        }
        return -1;
    }

    public SlotAssignment<T> getSlotAssignmentAt(int index) {
        return this.slotAssignments.get(index);
    }

    public int getSlotAssignmentCount() {
        return this.slotAssignments.size;
    }

    public void updateSlots() {
        Location<T> anchor = this.getAnchorPoint();
        this.positionOffset.set(anchor.getPosition());
        float orientationOffset = anchor.getOrientation();
        if (this.motionModerator != null) {
            this.positionOffset.sub(this.driftOffset.getPosition());
            orientationOffset -= this.driftOffset.getOrientation();
        }
        this.orientationMatrix.idt().rotateRad(anchor.getOrientation());
        for (int i = 0; i < this.slotAssignments.size; ++i) {
            SlotAssignment<T> slotAssignment = this.slotAssignments.get(i);
            Location relativeLoc = slotAssignment.member.getTargetLocation();
            this.pattern.calculateSlotLocation(relativeLoc, slotAssignment.slotNumber);
            T relativeLocPosition = relativeLoc.getPosition();
            if (relativeLocPosition instanceof Vector2) {
                ((Vector2)relativeLocPosition).mul(this.orientationMatrix);
            } else if (relativeLocPosition instanceof Vector3) {
                ((Vector3)relativeLocPosition).mul(this.orientationMatrix);
            }
            relativeLocPosition.add(this.positionOffset);
            relativeLoc.setOrientation(relativeLoc.getOrientation() + orientationOffset);
        }
        if (this.motionModerator != null) {
            this.motionModerator.updateAnchorPoint(anchor);
        }
    }
}

