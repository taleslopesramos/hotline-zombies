/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector;

public class PathSmootherRequest<N, V extends Vector<V>> {
    public boolean isNew = true;
    public int outputIndex;
    public int inputIndex;
    public SmoothableGraphPath<N, V> path;

    public void refresh(SmoothableGraphPath<N, V> path) {
        this.path = path;
        this.isNew = true;
    }
}

