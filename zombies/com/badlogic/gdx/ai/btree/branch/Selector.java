/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.SingleRunningChildBranch;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

public class Selector<E>
extends SingleRunningChildBranch<E> {
    public Selector() {
    }

    public /* varargs */ Selector(Task<E> ... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    public Selector(Array<Task<E>> tasks) {
        super(tasks);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        super.childFail(runningTask);
        if (++this.currentChildIndex < this.children.size) {
            this.run();
        } else {
            this.fail();
        }
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        super.childSuccess(runningTask);
        this.success();
    }
}

