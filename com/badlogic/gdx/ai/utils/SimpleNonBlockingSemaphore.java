/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

import com.badlogic.gdx.ai.utils.NonBlockingSemaphore;

public class SimpleNonBlockingSemaphore
implements NonBlockingSemaphore {
    String name;
    int maxResources;
    int acquiredResources;

    public SimpleNonBlockingSemaphore(String name, int maxResources) {
        this.name = name;
        this.maxResources = maxResources;
        this.acquiredResources = 0;
    }

    @Override
    public boolean acquire() {
        return this.acquire(1);
    }

    @Override
    public boolean acquire(int resources) {
        if (this.acquiredResources + resources <= this.maxResources) {
            this.acquiredResources += resources;
            return true;
        }
        return false;
    }

    @Override
    public boolean release() {
        return this.release(1);
    }

    @Override
    public boolean release(int resources) {
        if (this.acquiredResources - resources >= 0) {
            this.acquiredResources -= resources;
            return true;
        }
        return false;
    }

    public static class Factory
    implements NonBlockingSemaphore.Factory {
        @Override
        public NonBlockingSemaphore createSemaphore(String name, int maxResources) {
            return new SimpleNonBlockingSemaphore(name, maxResources);
        }
    }

}

