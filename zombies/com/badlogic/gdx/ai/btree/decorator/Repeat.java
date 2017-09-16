/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;

public class Repeat<E>
extends LoopDecorator<E> {
    @TaskAttribute
    public IntegerDistribution times;
    private int count;

    public Repeat() {
        this(null);
    }

    public Repeat(Task<E> child) {
        this(ConstantIntegerDistribution.NEGATIVE_ONE, child);
    }

    public Repeat(IntegerDistribution times, Task<E> child) {
        super(child);
        this.times = times;
    }

    @Override
    public void start() {
        this.count = this.times.nextInt();
    }

    @Override
    public boolean condition() {
        return this.loop && this.count != 0;
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        if (this.count > 0) {
            --this.count;
        }
        if (this.count == 0) {
            super.childSuccess(runningTask);
            this.loop = false;
        } else {
            this.loop = true;
        }
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.childSuccess(runningTask);
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        Repeat repeat = (Repeat)task;
        repeat.times = this.times;
        return super.copyTo(task);
    }
}

