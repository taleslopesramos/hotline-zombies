/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.math.MathUtils;

public final class TriangularFloatDistribution
extends FloatDistribution {
    private final float low;
    private final float high;
    private final float mode;

    public TriangularFloatDistribution(float high) {
        this(- high, high);
    }

    public TriangularFloatDistribution(float low, float high) {
        this(low, high, (low + high) * 0.5f);
    }

    public TriangularFloatDistribution(float low, float high, float mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public float nextFloat() {
        if (- this.low == this.high && this.mode == 0.0f) {
            return MathUtils.randomTriangular(this.high);
        }
        return MathUtils.randomTriangular(this.low, this.high, this.mode);
    }

    public float getLow() {
        return this.low;
    }

    public float getHigh() {
        return this.high;
    }

    public float getMode() {
        return this.mode;
    }
}

