/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.sched;

import com.badlogic.gdx.ai.sched.Schedulable;

public interface Scheduler
extends Schedulable {
    public void addWithAutomaticPhasing(Schedulable var1, int var2);

    public void add(Schedulable var1, int var2, int var3);
}

