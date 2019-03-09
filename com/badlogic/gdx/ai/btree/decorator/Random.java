/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution;
import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.math.MathUtils;

@TaskConstraint(minChildren=0, maxChildren=1)
public class Random<E>
extends Decorator<E> {
    @TaskAttribute
    public FloatDistribution success;
    private float p;

    public Random() {
        this(ConstantFloatDistribution.ZERO_POINT_FIVE);
    }

    public Random(Task<E> task) {
        this(ConstantFloatDistribution.ZERO_POINT_FIVE, task);
    }

    public Random(FloatDistribution success) {
        this.success = success;
    }

    public Random(FloatDistribution success, Task<E> task) {
        super(task);
        this.success = success;
    }

    @Override
    public void start() {
        this.p = this.success.nextFloat();
    }

    @Override
    public void run() {
        if (this.child != null) {
            super.run();
        } else {
            this.decide();
        }
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.decide();
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        this.decide();
    }

    private void decide() {
        if (MathUtils.random() <= this.p) {
            this.success();
        } else {
            this.fail();
        }
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        Random random = (Random)task;
        random.success = this.success;
        return super.copyTo(task);
    }
}

