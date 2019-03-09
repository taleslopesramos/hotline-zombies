/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.DoubleDistribution;

public final class ConstantDoubleDistribution
extends DoubleDistribution {
    public static final ConstantDoubleDistribution NEGATIVE_ONE = new ConstantDoubleDistribution(-1.0);
    public static final ConstantDoubleDistribution ZERO = new ConstantDoubleDistribution(0.0);
    public static final ConstantDoubleDistribution ONE = new ConstantDoubleDistribution(1.0);
    private final double value;

    public ConstantDoubleDistribution(double value) {
        this.value = value;
    }

    @Override
    public double nextDouble() {
        return this.value;
    }

    public double getValue() {
        return this.value;
    }
}

