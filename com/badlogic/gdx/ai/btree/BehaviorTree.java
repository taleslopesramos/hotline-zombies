/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

public class BehaviorTree<E>
extends Task<E> {
    private Task<E> rootTask;
    private E object;
    GuardEvaluator<E> guardEvalutor;
    public Array<Listener<E>> listeners;

    public BehaviorTree() {
        this(null, null);
    }

    public BehaviorTree(Task<E> rootTask) {
        this(rootTask, null);
    }

    public BehaviorTree(Task<E> rootTask, E object) {
        this.rootTask = rootTask;
        this.object = object;
        this.tree = this;
        this.guardEvalutor = new GuardEvaluator(this);
    }

    @Override
    public E getObject() {
        return this.object;
    }

    public void setObject(E object) {
        this.object = object;
    }

    @Override
    protected int addChildToTask(Task<E> child) {
        if (this.rootTask != null) {
            throw new IllegalStateException("A behavior tree cannot have more than one root task");
        }
        this.rootTask = child;
        return 0;
    }

    @Override
    public int getChildCount() {
        return this.rootTask == null ? 0 : 1;
    }

    @Override
    public Task<E> getChild(int i) {
        if (i == 0 && this.rootTask != null) {
            return this.rootTask;
        }
        throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + this.getChildCount());
    }

    @Override
    public void childRunning(Task<E> runningTask, Task<E> reporter) {
        this.running();
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.fail();
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        this.success();
    }

    public void step() {
        if (this.rootTask.status == Task.Status.RUNNING) {
            this.rootTask.run();
        } else {
            this.rootTask.setControl(this);
            this.rootTask.start();
            if (this.rootTask.checkGuard(this)) {
                this.rootTask.run();
            } else {
                this.rootTask.fail();
            }
        }
    }

    @Override
    public void run() {
    }

    @Override
    public void reset() {
        super.reset();
        this.tree = this;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        BehaviorTree tree = (BehaviorTree)task;
        tree.rootTask = this.rootTask.cloneTask();
        return task;
    }

    public void addListener(Listener<E> listener) {
        if (this.listeners == null) {
            this.listeners = new Array();
        }
        this.listeners.add(listener);
    }

    public void removeListener(Listener<E> listener) {
        if (this.listeners != null) {
            this.listeners.removeIndex(this.listeners.indexOf(listener, true));
        }
    }

    public void removeListeners() {
        if (this.listeners != null) {
            this.listeners.clear();
        }
    }

    public void notifyStatusUpdated(Task<E> task, Task.Status previousStatus) {
        for (Listener<E> listener : this.listeners) {
            listener.statusUpdated(task, previousStatus);
        }
    }

    public void notifyChildAdded(Task<E> task, int index) {
        for (Listener<E> listener : this.listeners) {
            listener.childAdded(task, index);
        }
    }

    public static interface Listener<E> {
        public void statusUpdated(Task<E> var1, Task.Status var2);

        public void childAdded(Task<E> var1, int var2);
    }

    private static final class GuardEvaluator<E>
    extends Task<E> {
        public GuardEvaluator(BehaviorTree<E> tree) {
            this.tree = tree;
        }

        @Override
        protected int addChildToTask(Task<E> child) {
            return 0;
        }

        @Override
        public int getChildCount() {
            return 0;
        }

        @Override
        public Task<E> getChild(int i) {
            return null;
        }

        @Override
        public void run() {
        }

        @Override
        public void childSuccess(Task<E> task) {
        }

        @Override
        public void childFail(Task<E> task) {
        }

        @Override
        public void childRunning(Task<E> runningTask, Task<E> reporter) {
        }

        @Override
        protected Task<E> copyTo(Task<E> task) {
            return null;
        }
    }

}

