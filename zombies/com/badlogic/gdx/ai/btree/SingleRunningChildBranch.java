/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.BranchTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public abstract class SingleRunningChildBranch<E>
extends BranchTask<E> {
    protected Task<E> runningChild;
    protected int currentChildIndex;
    protected Task<E>[] randomChildren;

    public SingleRunningChildBranch() {
    }

    public SingleRunningChildBranch(Array<Task<E>> tasks) {
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
    }

    @Override
    public void childFail(Task<E> task) {
        this.runningChild = null;
    }

    @Override
    public void run() {
        if (this.runningChild != null) {
            this.runningChild.run();
        } else if (this.currentChildIndex < this.children.size) {
            if (this.randomChildren != null) {
                int last = this.children.size - 1;
                if (this.currentChildIndex < last) {
                    int otherChildIndex = MathUtils.random(this.currentChildIndex, last);
                    Task<E> tmp = this.randomChildren[this.currentChildIndex];
                    this.randomChildren[this.currentChildIndex] = this.randomChildren[otherChildIndex];
                    this.randomChildren[otherChildIndex] = tmp;
                }
                this.runningChild = this.randomChildren[this.currentChildIndex];
            } else {
                this.runningChild = (Task)this.children.get(this.currentChildIndex);
            }
            this.runningChild.setControl(this);
            this.runningChild.start();
            if (!this.runningChild.checkGuard(this)) {
                this.runningChild.fail();
            }
            this.run();
        }
    }

    @Override
    public void start() {
        this.currentChildIndex = 0;
        this.runningChild = null;
    }

    @Override
    protected void cancelRunningChildren(int startIndex) {
        super.cancelRunningChildren(startIndex);
        this.runningChild = null;
    }

    @Override
    public void reset() {
        super.reset();
        this.currentChildIndex = 0;
        this.runningChild = null;
        this.randomChildren = null;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        SingleRunningChildBranch branch = (SingleRunningChildBranch)task;
        branch.randomChildren = null;
        return super.copyTo(task);
    }

    protected Task<E>[] createRandomChildren() {
        Task[] rndChildren = new Task[this.children.size];
        System.arraycopy(this.children.items, 0, rndChildren, 0, this.children.size);
        return rndChildren;
    }
}

