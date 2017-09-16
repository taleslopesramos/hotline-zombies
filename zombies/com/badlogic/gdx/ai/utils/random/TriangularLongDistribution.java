/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.LongDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularDoubleDistribution;

public final class TriangularLongDistribution
extends LongDistribution {
    private final long low;
    private final long high;
    private final double mode;

    public TriangularLongDistribution(long high) {
        this(- high, high);
    }

    public TriangularLongDistribution(long low, long high) {
        this(low, high, (double)(low + high) * 0.5);
    }

    public TriangularLongDistribution(long low, long high, double mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public long nextLong() {
        double r = - this.low == this.high && this.mode == 0.0 ? TriangularDoubleDistribution.randomTriangular(this.high) : TriangularDoubleDistribution.randomTriangular(this.low, this.high, this.mode);
        return Math.round(r);
    }

    public long getLow() {
        return this.low;
    }

    public long getHigh() {
        return this.high;
    }

    public double getMode() {
        return this.mode;
    }
}

