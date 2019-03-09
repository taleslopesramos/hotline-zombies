/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

public interface NonBlockingSemaphore {
    public boolean acquire();

    public boolean acquire(int var1);

    public boolean release();

    public boolean release(int var1);

    public static interface Factory {
        public NonBlockingSemaphore createSemaphore(String var1, int var2);
    }

}

