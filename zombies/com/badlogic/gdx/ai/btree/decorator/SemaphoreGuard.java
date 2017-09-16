/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.NonBlockingSemaphore;
import com.badlogic.gdx.ai.utils.NonBlockingSemaphoreRepository;

public class SemaphoreGuard<E>
extends Decorator<E> {
    @TaskAttribute(required=1)
    public String name;
    private transient NonBlockingSemaphore semaphore;
    private boolean semaphoreAcquired;

    public SemaphoreGuard() {
    }

    public SemaphoreGuard(Task<E> task) {
        super(task);
    }

    public SemaphoreGuard(String name) {
        this.name = name;
    }

    public SemaphoreGuard(String name, Task<E> task) {
        super(task);
        this.name = name;
    }

    @Override
    public void start() {
        if (this.semaphore == null) {
            this.semaphore = NonBlockingSemaphoreRepository.getSemaphore(this.name);
        }
        this.semaphoreAcquired = this.semaphore.acquire();
        super.start();
    }

    @Override
    public void run() {
        if (this.semaphoreAcquired) {
            super.run();
        } else {
            this.fail();
        }
    }

    @Override
    public void end() {
        if (this.semaphoreAcquired) {
            if (this.semaphore == null) {
                this.semaphore = NonBlockingSemaphoreRepository.getSemaphore(this.name);
            }
            this.semaphore.release();
            this.semaphoreAcquired = false;
        }
        super.end();
    }

    @Override
    public void reset() {
        super.reset();
        this.semaphore = null;
        this.semaphoreAcquired = false;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        SemaphoreGuard semaphoreGuard = (SemaphoreGuard)task;
        semaphoreGuard.name = this.name;
        semaphoreGuard.semaphore = null;
        semaphoreGuard.semaphoreAcquired = false;
        return super.copyTo(task);
    }
}

