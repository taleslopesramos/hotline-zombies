/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.Distribution;

public abstract class FloatDistribution
implements Distribution {
    @Override
    public int nextInt() {
        return (int)this.nextFloat();
    }

    @Override
    public long nextLong() {
        return (long)this.nextFloat();
    }

    @Override
    public double nextDouble() {
        return this.nextFloat();
    }
}

