/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.math.collision;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import java.io.Serializable;
import java.util.List;

public class BoundingBox
implements Serializable {
    private static final long serialVersionUID = -1286036817192127343L;
    private static final Vector3 tmpVector = new Vector3();
    public final Vector3 min = new Vector3();
    public final Vector3 max = new Vector3();
    private final Vector3 cnt = new Vector3();
    private final Vector3 dim = new Vector3();

    public Vector3 getCenter(Vector3 out) {
        return out.set(this.cnt);
    }

    public float getCenterX() {
        return this.cnt.x;
    }

    public float getCenterY() {
        return this.cnt.y;
    }

    public float getCenterZ() {
        return this.cnt.z;
    }

    public Vector3 getCorner000(Vector3 out) {
        return out.set(this.min.x, this.min.y, this.min.z);
    }

    public Vector3 getCorner001(Vector3 out) {
        return out.set(this.min.x, this.min.y, this.max.z);
    }

    public Vector3 getCorner010(Vector3 out) {
        return out.set(this.min.x, this.max.y, this.min.z);
    }

    public Vector3 getCorner011(Vector3 out) {
        return out.set(this.min.x, this.max.y, this.max.z);
    }

    public Vector3 getCorner100(Vector3 out) {
        return out.set(this.max.x, this.min.y, this.min.z);
    }

    public Vector3 getCorner101(Vector3 out) {
        return out.set(this.max.x, this.min.y, this.max.z);
    }

    public Vector3 getCorner110(Vector3 out) {
        return out.set(this.max.x, this.max.y, this.min.z);
    }

    public Vector3 getCorner111(Vector3 out) {
        return out.set(this.max.x, this.max.y, this.max.z);
    }

    public Vector3 getDimensions(Vector3 out) {
        return out.set(this.dim);
    }

    public float getWidth() {
        return this.dim.x;
    }

    public float getHeight() {
        return this.dim.y;
    }

    public float getDepth() {
        return this.dim.z;
    }

    public Vector3 getMin(Vector3 out) {
        return out.set(this.min);
    }

    public Vector3 getMax(Vector3 out) {
        return out.set(this.max);
    }

    public BoundingBox() {
        this.clr();
    }

    public BoundingBox(BoundingBox bounds) {
        this.set(bounds);
    }

    public BoundingBox(Vector3 minimum, Vector3 maximum) {
        this.set(minimum, maximum);
    }

    public BoundingBox set(BoundingBox bounds) {
        return this.set(bounds.min, bounds.max);
    }

    public BoundingBox set(Vector3 minimum, Vector3 maximum) {
        this.min.set(minimum.x < maximum.x ? minimum.x : maximum.x, minimum.y < maximum.y ? minimum.y : maximum.y, minimum.z < maximum.z ? minimum.z : maximum.z);
        this.max.set(minimum.x > maximum.x ? minimum.x : maximum.x, minimum.y > maximum.y ? minimum.y : maximum.y, minimum.z > maximum.z ? minimum.z : maximum.z);
        this.cnt.set(this.min).add(this.max).scl(0.5f);
        this.dim.set(this.max).sub(this.min);
        return this;
    }

    public BoundingBox set(Vector3[] points) {
        this.inf();
        for (Vector3 l_point : points) {
            this.ext(l_point);
        }
        return this;
    }

    public BoundingBox set(List<Vector3> points) {
        this.inf();
        for (Vector3 l_point : points) {
            this.ext(l_point);
        }
        return this;
    }

    public BoundingBox inf() {
        this.min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        this.max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        this.cnt.set(0.0f, 0.0f, 0.0f);
        this.dim.set(0.0f, 0.0f, 0.0f);
        return this;
    }

    public BoundingBox ext(Vector3 point) {
        return this.set(this.min.set(BoundingBox.min(this.min.x, point.x), BoundingBox.min(this.min.y, point.y), BoundingBox.min(this.min.z, point.z)), this.max.set(Math.max(this.max.x, point.x), Math.max(this.max.y, point.y), Math.max(this.max.z, point.z)));
    }

    public BoundingBox clr() {
        return this.set(this.min.set(0.0f, 0.0f, 0.0f), this.max.set(0.0f, 0.0f, 0.0f));
    }

    public boolean isValid() {
        return this.min.x < this.max.x && this.min.y < this.max.y && this.min.z < this.max.z;
    }

    public BoundingBox ext(BoundingBox a_bounds) {
        return this.set(this.min.set(BoundingBox.min(this.min.x, a_bounds.min.x), BoundingBox.min(this.min.y, a_bounds.min.y), BoundingBox.min(this.min.z, a_bounds.min.z)), this.max.set(BoundingBox.max(this.max.x, a_bounds.max.x), BoundingBox.max(this.max.y, a_bounds.max.y), BoundingBox.max(this.max.z, a_bounds.max.z)));
    }

    public BoundingBox ext(Vector3 center, float radius) {
        return this.set(this.min.set(BoundingBox.min(this.min.x, center.x - radius), BoundingBox.min(this.min.y, center.y - radius), BoundingBox.min(this.min.z, center.z - radius)), this.max.set(BoundingBox.max(this.max.x, center.x + radius), BoundingBox.max(this.max.y, center.y + radius), BoundingBox.max(this.max.z, center.z + radius)));
    }

    public BoundingBox ext(BoundingBox bounds, Matrix4 transform) {
        this.ext(tmpVector.set(bounds.min.x, bounds.min.y, bounds.min.z).mul(transform));
        this.ext(tmpVector.set(bounds.min.x, bounds.min.y, bounds.max.z).mul(transform));
        this.ext(tmpVector.set(bounds.min.x, bounds.max.y, bounds.min.z).mul(transform));
        this.ext(tmpVector.set(bounds.min.x, bounds.max.y, bounds.max.z).mul(transform));
        this.ext(tmpVector.set(bounds.max.x, bounds.min.y, bounds.min.z).mul(transform));
        this.ext(tmpVector.set(bounds.max.x, bounds.min.y, bounds.max.z).mul(transform));
        this.ext(tmpVector.set(bounds.max.x, bounds.max.y, bounds.min.z).mul(transform));
        this.ext(tmpVector.set(bounds.max.x, bounds.max.y, bounds.max.z).mul(transform));
        return this;
    }

    public BoundingBox mul(Matrix4 transform) {
        float x0 = this.min.x;
        float y0 = this.min.y;
        float z0 = this.min.z;
        float x1 = this.max.x;
        float y1 = this.max.y;
        float z1 = this.max.z;
        this.inf();
        this.ext(tmpVector.set(x0, y0, z0).mul(transform));
        this.ext(tmpVector.set(x0, y0, z1).mul(transform));
        this.ext(tmpVector.set(x0, y1, z0).mul(transform));
        this.ext(tmpVector.set(x0, y1, z1).mul(transform));
        this.ext(tmpVector.set(x1, y0, z0).mul(transform));
        this.ext(tmpVector.set(x1, y0, z1).mul(transform));
        this.ext(tmpVector.set(x1, y1, z0).mul(transform));
        this.ext(tmpVector.set(x1, y1, z1).mul(transform));
        return this;
    }

    public boolean contains(BoundingBox b) {
        return !this.isValid() || this.min.x <= b.min.x && this.min.y <= b.min.y && this.min.z <= b.min.z && this.max.x >= b.max.x && this.max.y >= b.max.y && this.max.z >= b.max.z;
    }

    public boolean intersects(BoundingBox b) {
        if (!this.isValid()) {
            return false;
        }
        float lx = Math.abs(this.cnt.x - b.cnt.x);
        float sumx = this.dim.x / 2.0f + b.dim.x / 2.0f;
        float ly = Math.abs(this.cnt.y - b.cnt.y);
        float sumy = this.dim.y / 2.0f + b.dim.y / 2.0f;
        float lz = Math.abs(this.cnt.z - b.cnt.z);
        float sumz = this.dim.z / 2.0f + b.dim.z / 2.0f;
        return lx <= sumx && ly <= sumy && lz <= sumz;
    }

    public boolean contains(Vector3 v) {
        return this.min.x <= v.x && this.max.x >= v.x && this.min.y <= v.y && this.max.y >= v.y && this.min.z <= v.z && this.max.z >= v.z;
    }

    public String toString() {
        return "[" + this.min + "|" + this.max + "]";
    }

    public BoundingBox ext(float x, float y, float z) {
        return this.set(this.min.set(BoundingBox.min(this.min.x, x), BoundingBox.min(this.min.y, y), BoundingBox.min(this.min.z, z)), this.max.set(BoundingBox.max(this.max.x, x), BoundingBox.max(this.max.y, y), BoundingBox.max(this.max.z, z)));
    }

    static final float min(float a, float b) {
        return a > b ? b : a;
    }

    static final float max(float a, float b) {
        return a > b ? a : b;
    }
}

