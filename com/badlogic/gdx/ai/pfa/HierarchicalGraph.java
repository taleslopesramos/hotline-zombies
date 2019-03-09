/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.Graph;

public interface HierarchicalGraph<N>
extends Graph<N> {
    public int getLevelCount();

    public void setLevel(int var1);

    public N convertNodeBetweenLevels(int var1, N var2, int var3);
}

