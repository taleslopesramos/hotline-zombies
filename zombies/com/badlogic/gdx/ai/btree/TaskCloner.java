/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task;

public interface TaskCloner {
    public <T> Task<T> cloneTask(Task<T> var1);
}

