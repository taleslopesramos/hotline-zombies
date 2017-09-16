/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;

@TaskConstraint(minChildren=1, maxChildren=1)
public abstract class Decorator<E>
extends Task<E> {
    protected Task<E> child;

    public Decorator() {
    }

    public Decorator(Task<E> child) {
        this.child = child;
    }

    @Override
    protected int addChildToTask(Task<E> child) {
        if (this.child != null) {
            throw new IllegalStateException("A decorator task cannot have more than one child");
        }
        this.child = child;
        return 0;
    }

    @Override
    public int getChildCount() {
        return this.child == null ? 0 : 1;
    }

    @Override
    public Task<E> getChild(int i) {
        if (i == 0 && this.child != null) {
            return this.child;
        }
        throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + this.getChildCount());
    }

    @Override
    public void run() {
        if (this.child.status == Task.Status.RUNNING) {
            this.child.run();
        } else {
            this.child.setControl(this);
            this.child.start();
            if (this.child.checkGuard(this)) {
                this.child.run();
            } else {
                this.child.fail();
            }
        }
    }

    @Override
    public void childRunning(Task<E> runningTask, Task<E> reporter) {
        this.running();
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.fail();
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        this.success();
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        if (this.child != null) {
            Decorator decorator = (Decorator)task;
            decorator.child = this.child.cloneTask();
        }
        return task;
    }
}

