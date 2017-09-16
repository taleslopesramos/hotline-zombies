/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.BranchTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

public class DynamicGuardSelector<E>
extends BranchTask<E> {
    protected Task<E> runningChild;

    public DynamicGuardSelector() {
    }

    public /* varargs */ DynamicGuardSelector(Task<E> ... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    public DynamicGuardSelector(Array<Task<E>> tasks) {
        super(tasks);
    }

    @Override
    public void childRunning(Task<E> task, Task<E> reporter) {
        this.runningChild = task;
        this.running();
    }

    @Override
    public void childSuccess(Task<E> task) {
        this.runningChild = null;
        this.success();
    }

    @Override
    public void childFail(Task<E> task) {
        this.runningChild = null;
        this.fail();
    }

    @Override
    public void run() {
        Task childToRun = null;
        int n = this.children.size;
        for (int i = 0; i < n; ++i) {
            Task child = (Task)this.children.get(i);
            if (!child.checkGuard(this)) continue;
            childToRun = child;
            break;
        }
        if (this.runningChild != null && this.runningChild != childToRun) {
            this.runningChild.cancel();
            this.runningChild = null;
        }
        if (childToRun == null) {
            this.fail();
        } else {
            if (this.runningChild == null) {
                this.runningChild = childToRun;
                this.runningChild.setControl(this);
                this.runningChild.start();
            }
            this.runningChild.run();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.runningChild = null;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        DynamicGuardSelector branch = (DynamicGuardSelector)task;
        branch.runningChild = null;
        return super.copyTo(task);
    }
}

