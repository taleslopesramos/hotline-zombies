/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.math.MathUtils;
import java.util.Random;

public final class GaussianFloatDistribution
extends FloatDistribution {
    public static final GaussianFloatDistribution STANDARD_NORMAL = new GaussianFloatDistribution(0.0f, 1.0f);
    private final float mean;
    private final float standardDeviation;

    public GaussianFloatDistribution(float mean, float standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public float nextFloat() {
        return this.mean + (float)MathUtils.random.nextGaussian() * this.standardDeviation;
    }

    public float getMean() {
        return this.mean;
    }

    public float getStandardDeviation() {
        return this.standardDeviation;
    }
}

