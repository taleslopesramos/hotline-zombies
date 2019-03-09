/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.sched;

import com.badlogic.gdx.ai.sched.Schedulable;
import com.badlogic.gdx.ai.sched.SchedulerBase;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class PriorityScheduler
extends SchedulerBase<PrioritySchedulableRecord> {
    protected int frame = 0;

    public PriorityScheduler(int dryRunFrames) {
        super(dryRunFrames);
    }

    @Override
    public void run(long timeToRun) {
        ++this.frame;
        this.runList.size = 0;
        float totalPriority = 0.0f;
        for (int i = 0; i < this.schedulableRecords.size; ++i) {
            PrioritySchedulableRecord record = (PrioritySchedulableRecord)this.schedulableRecords.get(i);
            if ((this.frame + record.phase) % record.frequency != 0) continue;
            this.runList.add(record);
            totalPriority += record.priority;
        }
        long lastTime = TimeUtils.nanoTime();
        int numToRun = this.runList.size;
        for (int i = 0; i < numToRun; ++i) {
            long currentTime = TimeUtils.nanoTime();
            PrioritySchedulableRecord record = (PrioritySchedulableRecord)this.runList.get(i);
            long availableTime = (long)((float)(timeToRun -= currentTime - lastTime) * record.priority / totalPriority);
            record.schedulable.run(availableTime);
            lastTime = currentTime;
        }
    }

    @Override
    public void addWithAutomaticPhasing(Schedulable schedulable, int frequency) {
        this.addWithAutomaticPhasing(schedulable, frequency, 1.0f);
    }

    public void addWithAutomaticPhasing(Schedulable schedulable, int frequency, float priority) {
        this.add(schedulable, frequency, this.calculatePhase(frequency), priority);
    }

    @Override
    public void add(Schedulable schedulable, int frequency, int phase) {
        this.add(schedulable, frequency, phase, 1.0f);
    }

    public void add(Schedulable schedulable, int frequency, int phase, float priority) {
        this.schedulableRecords.add(new PrioritySchedulableRecord(schedulable, frequency, phase, priority));
    }

    static class PrioritySchedulableRecord
    extends SchedulerBase.SchedulableRecord {
        float priority;

        PrioritySchedulableRecord(Schedulable schedulable, int frequency, int phase, float priority) {
            super(schedulable, frequency, phase);
            this.priority = priority;
        }
    }

}

