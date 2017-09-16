/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

public class AlwaysSucceed<E>
extends Decorator<E> {
    public AlwaysSucceed() {
    }

    public AlwaysSucceed(Task<E> task) {
        super(task);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.childSuccess(runningTask);
    }
}

