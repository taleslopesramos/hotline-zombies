/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.utils.Array;

public class RandomSelector<E>
extends Selector<E> {
    public RandomSelector() {
    }

    public /* varargs */ RandomSelector(Task<E> ... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    public RandomSelector(Array<Task<E>> tasks) {
        super(tasks);
    }

    @Override
    public void start() {
        super.start();
        if (this.randomChildren == null) {
            this.randomChildren = this.createRandomChildren();
        }
    }
}

