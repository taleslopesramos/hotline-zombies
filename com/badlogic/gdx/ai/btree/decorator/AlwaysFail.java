/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

public class AlwaysFail<E>
extends Decorator<E> {
    public AlwaysFail() {
    }

    public AlwaysFail(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        this.childFail(runningTask);
    }
}

