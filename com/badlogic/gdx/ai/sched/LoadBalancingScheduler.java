/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.sched;

import com.badlogic.gdx.ai.sched.Schedulable;
import com.badlogic.gdx.ai.sched.SchedulerBase;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class LoadBalancingScheduler
extends SchedulerBase<SchedulerBase.SchedulableRecord> {
    protected int frame = 0;

    public LoadBalancingScheduler(int dryRunFrames) {
        super(dryRunFrames);
    }

    @Override
    public void addWithAutomaticPhasing(Schedulable schedulable, int frequency) {
        this.add(schedulable, frequency, this.calculatePhase(frequency));
    }

    @Override
    public void add(Schedulable schedulable, int frequency, int phase) {
        this.schedulableRecords.add(new SchedulerBase.SchedulableRecord(schedulable, frequency, phase));
    }

    @Override
    public void run(long timeToRun) {
        ++this.frame;
        this.runList.size = 0;
        for (int i = 0; i < this.schedulableRecords.size; ++i) {
            SchedulerBase.SchedulableRecord record = (SchedulerBase.SchedulableRecord)this.schedulableRecords.get(i);
            if ((this.frame + record.phase) % record.frequency != 0) continue;
            this.runList.add(record);
        }
        long lastTime = TimeUtils.nanoTime();
        int numToRun = this.runList.size;
        for (int i = 0; i < numToRun; ++i) {
            long currentTime = TimeUtils.nanoTime();
            long availableTime = (timeToRun -= currentTime - lastTime) / (long)(numToRun - i);
            ((SchedulerBase.SchedulableRecord)this.runList.get((int)i)).schedulable.run(availableTime);
            lastTime = currentTime;
        }
    }
}

