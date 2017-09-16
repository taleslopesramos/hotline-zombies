/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.utils.Array;

public class RandomSequence<E>
extends Sequence<E> {
    public RandomSequence() {
    }

    public RandomSequence(Array<Task<E>> tasks) {
        super(tasks);
    }

    public /* varargs */ RandomSequence(Task<E> ... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    @Override
    public void start() {
        super.start();
        if (this.randomChildren == null) {
            this.randomChildren = this.createRandomChildren();
        }
    }
}

