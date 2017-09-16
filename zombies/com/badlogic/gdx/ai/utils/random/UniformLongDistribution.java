/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.LongDistribution;
import com.badlogic.gdx.math.MathUtils;
import java.util.Random;

public final class UniformLongDistribution
extends LongDistribution {
    private final long low;
    private final long high;

    public UniformLongDistribution(long high) {
        this(0, high);
    }

    public UniformLongDistribution(long low, long high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public long nextLong() {
        return this.low + (long)(MathUtils.random.nextDouble() * (double)(this.high - this.low));
    }

    public long getLow() {
        return this.low;
    }

    public long getHigh() {
        return this.high;
    }
}

