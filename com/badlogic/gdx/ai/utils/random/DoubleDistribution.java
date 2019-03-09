/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.Distribution;

public abstract class DoubleDistribution
implements Distribution {
    @Override
    public int nextInt() {
        return (int)this.nextDouble();
    }

    @Override
    public long nextLong() {
        return (long)this.nextDouble();
    }

    @Override
    public float nextFloat() {
        return (float)this.nextDouble();
    }
}

