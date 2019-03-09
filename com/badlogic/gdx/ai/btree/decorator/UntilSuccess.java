/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;

public class UntilSuccess<E>
extends LoopDecorator<E> {
    public UntilSuccess() {
    }

    public UntilSuccess(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        this.success();
        this.loop = false;
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.loop = true;
    }
}

