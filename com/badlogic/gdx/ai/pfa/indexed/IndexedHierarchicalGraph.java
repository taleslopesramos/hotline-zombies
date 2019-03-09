/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa.indexed;

import com.badlogic.gdx.ai.pfa.HierarchicalGraph;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;

public abstract class IndexedHierarchicalGraph<N>
implements IndexedGraph<N>,
HierarchicalGraph<N> {
    protected int levelCount;
    protected int level;

    public IndexedHierarchicalGraph(int levelCount) {
        this.levelCount = levelCount;
        this.level = 0;
    }

    @Override
    public int getLevelCount() {
        return this.levelCount;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public abstract N convertNodeBetweenLevels(int var1, N var2, int var3);
}

