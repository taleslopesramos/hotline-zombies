/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.DoubleDistribution;
import com.badlogic.gdx.math.MathUtils;
import java.util.Random;

public final class GaussianDoubleDistribution
extends DoubleDistribution {
    public static final GaussianDoubleDistribution STANDARD_NORMAL = new GaussianDoubleDistribution(0.0, 1.0);
    private final double mean;
    private final double standardDeviation;

    public GaussianDoubleDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public double nextDouble() {
        return this.mean + MathUtils.random.nextGaussian() * this.standardDeviation;
    }

    public double getMean() {
        return this.mean;
    }

    public double getStandardDeviation() {
        return this.standardDeviation;
    }
}

