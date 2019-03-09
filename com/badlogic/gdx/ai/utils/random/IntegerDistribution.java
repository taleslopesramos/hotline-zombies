/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.Distribution;

public abstract class IntegerDistribution
implements Distribution {
    @Override
    public long nextLong() {
        return this.nextInt();
    }

    @Override
    public float nextFloat() {
        return this.nextInt();
    }

    @Override
    public double nextDouble() {
        return this.nextInt();
    }
}

