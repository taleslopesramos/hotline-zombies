/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.leaf;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class Failure<E>
extends LeafTask<E> {
    @Override
    public Task.Status execute() {
        return Task.Status.FAILED;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        return task;
    }
}

