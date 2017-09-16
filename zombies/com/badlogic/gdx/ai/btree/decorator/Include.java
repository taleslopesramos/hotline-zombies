/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.TaskCloneException;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;

@TaskConstraint(minChildren=0, maxChildren=0)
public class Include<E>
extends Decorator<E> {
    @TaskAttribute(required=1)
    public String subtree;
    @TaskAttribute
    public boolean lazy;

    public Include() {
    }

    public Include(String subtree) {
        this.subtree = subtree;
    }

    public Include(String subtree, boolean lazy) {
        this.subtree = subtree;
        this.lazy = lazy;
    }

    @Override
    public void start() {
        if (!this.lazy) {
            throw new UnsupportedOperationException("A non-lazy " + Include.class.getSimpleName() + " isn't meant to be run!");
        }
        if (this.child == null) {
            this.addChild(this.createSubtreeRootTask());
        }
    }

    @Override
    public Task<E> cloneTask() {
        if (this.lazy) {
            return super.cloneTask();
        }
        return this.createSubtreeRootTask();
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        if (!this.lazy) {
            throw new TaskCloneException("A non-lazy " + this.getClass().getSimpleName() + " should never be copied.");
        }
        Include include = (Include)task;
        include.subtree = this.subtree;
        include.lazy = this.lazy;
        return task;
    }

    private Task<E> createSubtreeRootTask() {
        return BehaviorTreeLibraryManager.getInstance().createRootTask(this.subtree);
    }
}

