/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.leaf;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution;
import com.badlogic.gdx.ai.utils.random.FloatDistribution;

public class Wait<E>
extends LeafTask<E> {
    @TaskAttribute(required=1)
    public FloatDistribution seconds;
    private float startTime;
    private float timeout;

    public Wait() {
        this(ConstantFloatDistribution.ZERO);
    }

    public Wait(float seconds) {
        this(new ConstantFloatDistribution(seconds));
    }

    public Wait(FloatDistribution seconds) {
        this.seconds = seconds;
    }

    @Override
    public void start() {
        this.timeout = this.seconds.nextFloat();
        this.startTime = GdxAI.getTimepiece().getTime();
    }

    @Override
    public Task.Status execute() {
        return GdxAI.getTimepiece().getTime() - this.startTime < this.timeout ? Task.Status.RUNNING : Task.Status.SUCCEEDED;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        ((Wait)task).seconds = this.seconds;
        return task;
    }
}

