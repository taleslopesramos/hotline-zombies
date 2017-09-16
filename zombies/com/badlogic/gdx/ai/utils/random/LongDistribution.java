/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.Distribution;

public abstract class LongDistribution
implements Distribution {
    @Override
    public int nextInt() {
        return (int)this.nextLong();
    }

    @Override
    public float nextFloat() {
        return this.nextLong();
    }

    @Override
    public double nextDouble() {
        return this.nextLong();
    }
}

