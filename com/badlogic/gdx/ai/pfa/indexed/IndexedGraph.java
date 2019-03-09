/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa.indexed;

import com.badlogic.gdx.ai.pfa.Graph;

public interface IndexedGraph<N>
extends Graph<N> {
    public int getIndex(N var1);

    public int getNodeCount();
}

