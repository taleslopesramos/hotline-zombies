/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.sched;

import com.badlogic.gdx.ai.sched.Schedulable;
import com.badlogic.gdx.ai.sched.Scheduler;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public abstract class SchedulerBase<T extends SchedulableRecord>
implements Scheduler {
    protected Array<T> schedulableRecords = new Array();
    protected Array<T> runList = new Array();
    protected IntArray phaseCounters = new IntArray();
    protected int dryRunFrames;

    public SchedulerBase(int dryRunFrames) {
        this.dryRunFrames = dryRunFrames;
    }

    protected int calculatePhase(int frequency) {
        int i;
        if (frequency > this.phaseCounters.size) {
            this.phaseCounters.ensureCapacity(frequency - this.phaseCounters.size);
        }
        int[] items = this.phaseCounters.items;
        this.phaseCounters.size = frequency;
        for (int i2 = 0; i2 < frequency; ++i2) {
            items[i2] = 0;
        }
        for (int frame = 0; frame < this.dryRunFrames; ++frame) {
            int slot = frame % frequency;
            for (i = 0; i < this.schedulableRecords.size; ++i) {
                SchedulableRecord record = (SchedulableRecord)this.schedulableRecords.get(i);
                if ((frame - record.phase) % record.frequency != 0) continue;
                int[] arrn = items;
                int n = slot;
                arrn[n] = arrn[n] + 1;
            }
        }
        int minValue = Integer.MAX_VALUE;
        int minValueAt = -1;
        for (i = 0; i < frequency; ++i) {
            if (items[i] >= minValue) continue;
            minValue = items[i];
            minValueAt = i;
        }
        return minValueAt;
    }

    protected static class SchedulableRecord {
        Schedulable schedulable;
        int frequency;
        int phase;

        SchedulableRecord(Schedulable schedulable, int frequency, int phase) {
            this.schedulable = schedulable;
            this.frequency = frequency;
            this.phase = phase;
        }
    }

}

