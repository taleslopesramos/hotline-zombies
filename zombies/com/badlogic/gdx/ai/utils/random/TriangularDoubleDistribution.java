/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.ai.utils.random.DoubleDistribution;
import com.badlogic.gdx.math.MathUtils;
import java.util.Random;

public final class TriangularDoubleDistribution
extends DoubleDistribution {
    private final double low;
    private final double high;
    private final double mode;

    public TriangularDoubleDistribution(double high) {
        this(- high, high);
    }

    public TriangularDoubleDistribution(double low, double high) {
        this(low, high, (low + high) * 0.5);
    }

    public TriangularDoubleDistribution(double low, double high, double mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public double nextDouble() {
        if (- this.low == this.high && this.mode == 0.0) {
            return TriangularDoubleDistribution.randomTriangular(this.high);
        }
        return TriangularDoubleDistribution.randomTriangular(this.low, this.high, this.mode);
    }

    public double getLow() {
        return this.low;
    }

    public double getHigh() {
        return this.high;
    }

    public double getMode() {
        return this.mode;
    }

    static double randomTriangular(double high) {
        return (MathUtils.random.nextDouble() - MathUtils.random.nextDouble()) * high;
    }

    static double randomTriangular(double low, double high, double mode) {
        double d;
        double u = MathUtils.random.nextDouble();
        if (u <= (mode - low) / (d = high - low)) {
            return low + Math.sqrt(u * d * (mode - low));
        }
        return high - Math.sqrt((1.0 - u) * d * (high - mode));
    }
}

