/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.FloatDistribution;

public final class ConstantFloatDistribution
extends FloatDistribution {
    public static final ConstantFloatDistribution NEGATIVE_ONE = new ConstantFloatDistribution(-1.0f);
    public static final ConstantFloatDistribution ZERO = new ConstantFloatDistribution(0.0f);
    public static final ConstantFloatDistribution ONE = new ConstantFloatDistribution(1.0f);
    public static final ConstantFloatDistribution ZERO_POINT_FIVE = new ConstantFloatDistribution(0.5f);
    private final float value;

    public ConstantFloatDistribution(float value) {
        this.value = value;
    }

    @Override
    public float nextFloat() {
        return this.value;
    }

    public float getValue() {
        return this.value;
    }
}

