/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.IntegerDistribution;

public final class ConstantIntegerDistribution
extends IntegerDistribution {
    public static final ConstantIntegerDistribution NEGATIVE_ONE = new ConstantIntegerDistribution(-1);
    public static final ConstantIntegerDistribution ZERO = new ConstantIntegerDistribution(0);
    public static final ConstantIntegerDistribution ONE = new ConstantIntegerDistribution(1);
    private final int value;

    public ConstantIntegerDistribution(int value) {
        this.value = value;
    }

    @Override
    public int nextInt() {
        return this.value;
    }

    public int getValue() {
        return this.value;
    }
}

