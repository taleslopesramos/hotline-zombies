/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

public interface Connection<N> {
    public float getCost();

    public N getFromNode();

    public N getToNode();
}

