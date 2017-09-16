/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.DoubleDistribution;
import com.badlogic.gdx.math.MathUtils;
import java.util.Random;

public final class UniformDoubleDistribution
extends DoubleDistribution {
    private final double low;
    private final double high;

    public UniformDoubleDistribution(double high) {
        this(0.0, high);
    }

    public UniformDoubleDistribution(double low, double high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public double nextDouble() {
        return this.low + MathUtils.random.nextDouble() * (this.high - this.low);
    }

    public double getLow() {
        return this.low;
    }

    public double getHigh() {
        return this.high;
    }
}

