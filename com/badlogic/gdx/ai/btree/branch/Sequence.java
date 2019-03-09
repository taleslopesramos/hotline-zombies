/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.SingleRunningChildBranch;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

public class Sequence<E>
extends SingleRunningChildBranch<E> {
    public Sequence() {
    }

    public Sequence(Array<Task<E>> tasks) {
        super(tasks);
    }

    public /* varargs */ Sequence(Task<E> ... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        super.childSuccess(runningTask);
        if (++this.currentChildIndex < this.children.size) {
            this.run();
        } else {
            this.success();
        }
    }

    @Override
    public void childFail(Task<E> runningTask) {
        super.childFail(runningTask);
        this.fail();
    }
}

