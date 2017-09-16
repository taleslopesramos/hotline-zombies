/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.utils.Array;

@TaskConstraint(minChildren=1)
public abstract class BranchTask<E>
extends Task<E> {
    protected Array<Task<E>> children;

    public BranchTask() {
        this(new Array<Task<E>>());
    }

    public BranchTask(Array<Task<E>> tasks) {
        this.children = tasks;
    }

    @Override
    protected int addChildToTask(Task<E> child) {
        this.children.add(child);
        return this.children.size - 1;
    }

    @Override
    public int getChildCount() {
        return this.children.size;
    }

    @Override
    public Task<E> getChild(int i) {
        return this.children.get(i);
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        BranchTask branch = (BranchTask)task;
        if (this.children != null) {
            for (int i = 0; i < this.children.size; ++i) {
                branch.children.add(this.children.get(i).cloneTask());
            }
        }
        return task;
    }
}

