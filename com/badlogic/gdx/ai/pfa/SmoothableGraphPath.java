/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.Vector;

public interface SmoothableGraphPath<N, V extends Vector<V>>
extends GraphPath<N> {
    public V getNodePosition(int var1);

    public void swapNodes(int var1, int var2);

    public void truncatePath(int var1);
}

