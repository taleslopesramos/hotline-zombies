/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.math.MathUtils;

public final class UniformFloatDistribution
extends FloatDistribution {
    private final float low;
    private final float high;

    public UniformFloatDistribution(float high) {
        this(0.0f, high);
    }

    public UniformFloatDistribution(float low, float high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public float nextFloat() {
        return MathUtils.random(this.low, this.high);
    }

    public float getLow() {
        return this.low;
    }

    public float getHigh() {
        return this.high;
    }
}

