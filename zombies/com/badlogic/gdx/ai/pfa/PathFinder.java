/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;

public interface PathFinder<N> {
    public boolean searchConnectionPath(N var1, N var2, Heuristic<N> var3, GraphPath<Connection<N>> var4);

    public boolean searchNodePath(N var1, N var2, Heuristic<N> var3, GraphPath<N> var4);

    public boolean search(PathFinderRequest<N> var1, long var2);
}

