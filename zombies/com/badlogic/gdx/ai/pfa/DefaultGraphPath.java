/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;

public class DefaultGraphPath<N>
implements GraphPath<N> {
    public final Array<N> nodes;

    public DefaultGraphPath() {
        this(new Array());
    }

    public DefaultGraphPath(int capacity) {
        this(new Array(capacity));
    }

    public DefaultGraphPath(Array<N> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void clear() {
        this.nodes.clear();
    }

    @Override
    public int getCount() {
        return this.nodes.size;
    }

    @Override
    public void add(N node) {
        this.nodes.add(node);
    }

    @Override
    public N get(int index) {
        return this.nodes.get(index);
    }

    @Override
    public void reverse() {
        this.nodes.reverse();
    }

    @Override
    public Iterator<N> iterator() {
        return this.nodes.iterator();
    }
}

