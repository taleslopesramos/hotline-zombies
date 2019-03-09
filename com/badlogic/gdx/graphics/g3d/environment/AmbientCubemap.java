/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.graphics.g3d.environment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AmbientCubemap {
    public final float[] data;

    private static final float clamp(float v) {
        return v < 0.0f ? 0.0f : (v > 1.0f ? 1.0f : v);
    }

    public AmbientCubemap() {
        this.data = new float[18];
    }

    public AmbientCubemap(float[] copyFrom) {
        if (copyFrom.length != 18) {
            throw new GdxRuntimeException("Incorrect array size");
        }
        this.data = new float[copyFrom.length];
        System.arraycopy(copyFrom, 0, this.data, 0, this.data.length);
    }

    public AmbientCubemap(AmbientCubemap copyFrom) {
        this(copyFrom.data);
    }

    public AmbientCubemap set(float[] values) {
        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] = values[i];
        }
        return this;
    }

    public AmbientCubemap set(AmbientCubemap other) {
        return this.set(other.data);
    }

    public AmbientCubemap set(Color color) {
        return this.set(color.r, color.g, color.b);
    }

    public AmbientCubemap set(float r, float g, float b) {
        int idx = 0;
        while (idx < this.data.length) {
            this.data[idx++] = r;
            this.data[idx++] = g;
            this.data[idx++] = b;
        }
        return this;
    }

    public Color getColor(Color out, int side) {
        return out.set(this.data[side], this.data[side + 1], this.data[(side *= 3) + 2], 1.0f);
    }

    public AmbientCubemap clear() {
        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] = 0.0f;
        }
        return this;
    }

    public AmbientCubemap clamp() {
        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] = AmbientCubemap.clamp(this.data[i]);
        }
        return this;
    }

    public AmbientCubemap add(float r, float g, float b) {
        int idx = 0;
        while (idx < this.data.length) {
            float[] arrf = this.data;
            int n = idx++;
            arrf[n] = arrf[n] + r;
            float[] arrf2 = this.data;
            int n2 = idx++;
            arrf2[n2] = arrf2[n2] + g;
            float[] arrf3 = this.data;
            int n3 = idx++;
            arrf3[n3] = arrf3[n3] + b;
        }
        return this;
    }

    public AmbientCubemap add(Color color) {
        return this.add(color.r, color.g, color.b);
    }

    public AmbientCubemap add(float r, float g, float b, float x, float y, float z) {
        float x2 = x * x;
        float y2 = y * y;
        float z2 = z * z;
        float d = x2 + y2 + z2;
        if (d == 0.0f) {
            return this;
        }
        d = 1.0f / d * (d + 1.0f);
        float rd = r * d;
        float gd = g * d;
        float bd = b * d;
        int idx = x > 0.0f ? 0 : 3;
        float[] arrf = this.data;
        int n = idx;
        arrf[n] = arrf[n] + x2 * rd;
        float[] arrf2 = this.data;
        int n2 = idx + 1;
        arrf2[n2] = arrf2[n2] + x2 * gd;
        float[] arrf3 = this.data;
        int n3 = idx + 2;
        arrf3[n3] = arrf3[n3] + x2 * bd;
        idx = y > 0.0f ? 6 : 9;
        float[] arrf4 = this.data;
        int n4 = idx;
        arrf4[n4] = arrf4[n4] + y2 * rd;
        float[] arrf5 = this.data;
        int n5 = idx + 1;
        arrf5[n5] = arrf5[n5] + y2 * gd;
        float[] arrf6 = this.data;
        int n6 = idx + 2;
        arrf6[n6] = arrf6[n6] + y2 * bd;
        idx = z > 0.0f ? 12 : 15;
        float[] arrf7 = this.data;
        int n7 = idx;
        arrf7[n7] = arrf7[n7] + z2 * rd;
        float[] arrf8 = this.data;
        int n8 = idx + 1;
        arrf8[n8] = arrf8[n8] + z2 * gd;
        float[] arrf9 = this.data;
        int n9 = idx + 2;
        arrf9[n9] = arrf9[n9] + z2 * bd;
        return this;
    }

    public AmbientCubemap add(Color color, Vector3 direction) {
        return this.add(color.r, color.g, color.b, direction.x, direction.y, direction.z);
    }

    public AmbientCubemap add(float r, float g, float b, Vector3 direction) {
        return this.add(r, g, b, direction.x, direction.y, direction.z);
    }

    public AmbientCubemap add(Color color, float x, float y, float z) {
        return this.add(color.r, color.g, color.b, x, y, z);
    }

    public AmbientCubemap add(Color color, Vector3 point, Vector3 target) {
        return this.add(color.r, color.g, color.b, target.x - point.x, target.y - point.y, target.z - point.z);
    }

    public AmbientCubemap add(Color color, Vector3 point, Vector3 target, float intensity) {
        float t = intensity / (1.0f + target.dst(point));
        return this.add(color.r * t, color.g * t, color.b * t, target.x - point.x, target.y - point.y, target.z - point.z);
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < this.data.length; i += 3) {
            result = result + Float.toString(this.data[i]) + ", " + Float.toString(this.data[i + 1]) + ", " + Float.toString(this.data[i + 2]) + "\n";
        }
        return result;
    }
}

