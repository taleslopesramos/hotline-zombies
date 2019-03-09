/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.badlogic.gdx.math.MathUtils;

public final class UniformIntegerDistribution
extends IntegerDistribution {
    private final int low;
    private final int high;

    public UniformIntegerDistribution(int high) {
        this(0, high);
    }

    public UniformIntegerDistribution(int low, int high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public int nextInt() {
        return MathUtils.random(this.low, this.high);
    }

    public int getLow() {
        return this.low;
    }

    public int getHigh() {
        return this.high;
    }
}

