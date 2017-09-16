/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.badlogic.gdx.math.MathUtils;

public final class TriangularIntegerDistribution
extends IntegerDistribution {
    private final int low;
    private final int high;
    private final float mode;

    public TriangularIntegerDistribution(int high) {
        this(- high, high);
    }

    public TriangularIntegerDistribution(int low, int high) {
        this(low, high, (float)(low + high) * 0.5f);
    }

    public TriangularIntegerDistribution(int low, int high, float mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public int nextInt() {
        float r = - this.low == this.high && this.mode == 0.0f ? MathUtils.randomTriangular(this.high) : MathUtils.randomTriangular(this.low, this.high, this.mode);
        return Math.round(r);
    }

    public int getLow() {
        return this.low;
    }

    public int getHigh() {
        return this.high;
    }

    public float getMode() {
        return this.mode;
    }
}

