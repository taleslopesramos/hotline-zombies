/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

public class Invert<E>
extends Decorator<E> {
    public Invert() {
    }

    public Invert(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        super.childFail(runningTask);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        super.childSuccess(runningTask);
    }
}

