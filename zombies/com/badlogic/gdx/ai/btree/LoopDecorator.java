/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

public abstract class LoopDecorator<E>
extends Decorator<E> {
    protected boolean loop;

    public LoopDecorator() {
    }

    public LoopDecorator(Task<E> child) {
        super(child);
    }

    public boolean condition() {
        return this.loop;
    }

    @Override
    public void run() {
        this.loop = true;
        while (this.condition()) {
            if (this.child.status == Task.Status.RUNNING) {
                this.child.run();
                continue;
            }
            this.child.setControl(this);
            this.child.start();
            if (this.child.checkGuard(this)) {
                this.child.run();
                continue;
            }
            this.child.fail();
        }
    }

    @Override
    public void childRunning(Task<E> runningTask, Task<E> reporter) {
        super.childRunning(runningTask, reporter);
        this.loop = false;
    }
}

