/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.TaskCloneException;
import com.badlogic.gdx.ai.btree.TaskCloner;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

@TaskConstraint
public abstract class Task<E> {
    public static TaskCloner TASK_CLONER = null;
    protected Status status = Status.FRESH;
    protected Task<E> control;
    protected BehaviorTree<E> tree;
    protected Task<E> guard;

    public final int addChild(Task<E> child) {
        int index = this.addChildToTask(child);
        if (this.tree != null && this.tree.listeners != null) {
            this.tree.notifyChildAdded(this, index);
        }
        return index;
    }

    protected abstract int addChildToTask(Task<E> var1);

    public abstract int getChildCount();

    public abstract Task<E> getChild(int var1);

    public E getObject() {
        if (this.tree == null) {
            throw new IllegalStateException("This task has never run");
        }
        return this.tree.getObject();
    }

    public Task<E> getGuard() {
        return this.guard;
    }

    public void setGuard(Task<E> guard) {
        this.guard = guard;
    }

    public final Status getStatus() {
        return this.status;
    }

    public final void setControl(Task<E> control) {
        this.control = control;
        this.tree = control.tree;
    }

    public boolean checkGuard(Task<E> control) {
        if (this.guard == null) {
            return true;
        }
        if (!this.guard.checkGuard(control)) {
            return false;
        }
        this.guard.setControl(control.tree.guardEvalutor);
        this.guard.start();
        this.guard.run();
        switch (this.guard.getStatus()) {
            case SUCCEEDED: {
                return true;
            }
            case FAILED: {
                return false;
            }
        }
        throw new IllegalStateException("Illegal guard status '" + (Object)((Object)this.guard.getStatus()) + "'. Guards must either succeed or fail in one step.");
    }

    public void start() {
    }

    public void end() {
    }

    public abstract void run();

    public final void running() {
        Status previousStatus = this.status;
        this.status = Status.RUNNING;
        if (this.tree.listeners != null && this.tree.listeners.size > 0) {
            this.tree.notifyStatusUpdated(this, previousStatus);
        }
        if (this.control != null) {
            this.control.childRunning(this, this);
        }
    }

    public final void success() {
        Status previousStatus = this.status;
        this.status = Status.SUCCEEDED;
        if (this.tree.listeners != null && this.tree.listeners.size > 0) {
            this.tree.notifyStatusUpdated(this, previousStatus);
        }
        this.end();
        if (this.control != null) {
            this.control.childSuccess(this);
        }
    }

    public final void fail() {
        Status previousStatus = this.status;
        this.status = Status.FAILED;
        if (this.tree.listeners != null && this.tree.listeners.size > 0) {
            this.tree.notifyStatusUpdated(this, previousStatus);
        }
        this.end();
        if (this.control != null) {
            this.control.childFail(this);
        }
    }

    public abstract void childSuccess(Task<E> var1);

    public abstract void childFail(Task<E> var1);

    public abstract void childRunning(Task<E> var1, Task<E> var2);

    public final void cancel() {
        this.cancelRunningChildren(0);
        Status previousStatus = this.status;
        this.status = Status.CANCELLED;
        if (this.tree.listeners != null && this.tree.listeners.size > 0) {
            this.tree.notifyStatusUpdated(this, previousStatus);
        }
        this.end();
    }

    protected void cancelRunningChildren(int startIndex) {
        int n = this.getChildCount();
        for (int i = startIndex; i < n; ++i) {
            Task<E> child = this.getChild(i);
            if (child.status != Status.RUNNING) continue;
            child.cancel();
        }
    }

    public void reset() {
        if (this.status == Status.RUNNING) {
            this.cancel();
        }
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            this.getChild(i).reset();
        }
        this.status = Status.FRESH;
        this.tree = null;
        this.control = null;
    }

    public Task<E> cloneTask() {
        if (TASK_CLONER != null) {
            try {
                return TASK_CLONER.cloneTask(this);
            }
            catch (Throwable t) {
                throw new TaskCloneException(t);
            }
        }
        try {
            Task<E> clone = this.copyTo((Task)ClassReflection.newInstance(this.getClass()));
            clone.guard = this.guard == null ? null : this.guard.cloneTask();
            return clone;
        }
        catch (ReflectionException e) {
            throw new TaskCloneException(e);
        }
    }

    protected abstract Task<E> copyTo(Task<E> var1);

    public static enum Status {
        FRESH,
        RUNNING,
        FAILED,
        SUCCEEDED,
        CANCELLED;
        

        private Status() {
        }
    }

}

