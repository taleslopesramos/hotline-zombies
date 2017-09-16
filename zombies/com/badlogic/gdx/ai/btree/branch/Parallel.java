/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.BranchTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.utils.Array;

public class Parallel<E>
extends BranchTask<E> {
    @TaskAttribute
    public Policy policy;
    private boolean noRunningTasks;
    private Boolean lastResult;
    private int currentChildIndex;

    public Parallel() {
        this(new Array<Task<E>>());
    }

    public /* varargs */ Parallel(Task<E> ... tasks) {
        this(new Array<Task<E>>(tasks));
    }

    public Parallel(Array<Task<E>> tasks) {
        this(Policy.Sequence, tasks);
    }

    public Parallel(Policy policy) {
        this(policy, new Array<Task<E>>());
    }

    public /* varargs */ Parallel(Policy policy, Task<E> ... tasks) {
        this(policy, new Array<Task<E>>(tasks));
    }

    public Parallel(Policy policy, Array<Task<E>> tasks) {
        super(tasks);
        this.policy = policy;
        this.noRunningTasks = true;
    }

    @Override
    public void run() {
        this.noRunningTasks = true;
        this.lastResult = null;
        this.currentChildIndex = 0;
        while (this.currentChildIndex < this.children.size) {
            Task child = (Task)this.children.get(this.currentChildIndex);
            if (child.getStatus() == Task.Status.RUNNING) {
                child.run();
            } else {
                child.setControl(this);
                child.start();
                if (child.checkGuard(this)) {
                    child.run();
                } else {
                    child.fail();
                }
            }
            if (this.lastResult != null) {
                this.cancelRunningChildren(this.noRunningTasks ? this.currentChildIndex + 1 : 0);
                if (this.lastResult.booleanValue()) {
                    this.success();
                } else {
                    this.fail();
                }
                return;
            }
            ++this.currentChildIndex;
        }
        this.running();
    }

    @Override
    public void childRunning(Task<E> task, Task<E> reporter) {
        this.noRunningTasks = false;
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        this.lastResult = this.policy.onChildSuccess(this);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        this.lastResult = this.policy.onChildFail(this);
    }

    @Override
    public void reset() {
        super.reset();
        this.noRunningTasks = true;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        Parallel parallel = (Parallel)task;
        parallel.policy = this.policy;
        return super.copyTo(task);
    }

    static /* synthetic */ Array access$300(Parallel x0) {
        return x0.children;
    }

    static /* synthetic */ Array access$400(Parallel x0) {
        return x0.children;
    }

    public static enum Policy {
        Sequence{

            @Override
            public Boolean onChildSuccess(Parallel<?> parallel) {
                return parallel.noRunningTasks && parallel.currentChildIndex == Parallel.access$300(parallel).size - 1 ? Boolean.TRUE : null;
            }

            @Override
            public Boolean onChildFail(Parallel<?> parallel) {
                return Boolean.FALSE;
            }
        }
        ,
        Selector{

            @Override
            public Boolean onChildSuccess(Parallel<?> parallel) {
                return Boolean.TRUE;
            }

            @Override
            public Boolean onChildFail(Parallel<?> parallel) {
                return parallel.noRunningTasks && parallel.currentChildIndex == Parallel.access$400(parallel).size - 1 ? Boolean.FALSE : null;
            }
        };
        

        private Policy() {
        }

        public abstract Boolean onChildSuccess(Parallel<?> var1);

        public abstract Boolean onChildFail(Parallel<?> var1);

    }

}

