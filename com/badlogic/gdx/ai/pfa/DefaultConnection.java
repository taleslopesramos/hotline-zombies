/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.Connection;

public class DefaultConnection<N>
implements Connection<N> {
    protected N fromNode;
    protected N toNode;

    public DefaultConnection(N fromNode, N toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public float getCost() {
        return 1.0f;
    }

    @Override
    public N getFromNode() {
        return this.fromNode;
    }

    @Override
    public N getToNode() {
        return this.toNode;
    }
}

