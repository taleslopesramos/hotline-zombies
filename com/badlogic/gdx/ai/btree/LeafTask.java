/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;

@TaskConstraint(minChildren=0, maxChildren=0)
public abstract class LeafTask<E>
extends Task<E> {
    public abstract Task.Status execute();

    @Override
    public final void run() {
        Task.Status result = this.execute();
        if (result == null) {
            throw new IllegalStateException("Invalid status 'null' returned by the execute method");
        }
        switch (result) {
            case SUCCEEDED: {
                this.success();
                return;
            }
            case FAILED: {
                this.fail();
                return;
            }
            case RUNNING: {
                this.running();
                return;
            }
        }
        throw new IllegalStateException("Invalid status '" + result.name() + "' returned by the execute method");
    }

    @Override
    protected int addChildToTask(Task<E> child) {
        throw new IllegalStateException("A leaf task cannot have any children");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public Task<E> getChild(int i) {
        throw new IndexOutOfBoundsException("A leaf task can not have any child");
    }

    @Override
    public final void childRunning(Task<E> runningTask, Task<E> reporter) {
    }

    @Override
    public final void childFail(Task<E> runningTask) {
    }

    @Override
    public final void childSuccess(Task<E> runningTask) {
    }

}

