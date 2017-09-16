/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.utils.paths;

import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public class LinePath<T extends Vector<T>>
implements Path<T, LinePathParam> {
    private Array<Segment<T>> segments;
    private boolean isOpen;
    private float pathLength;
    private T nearestPointOnCurrentSegment;
    private T nearestPointOnPath;
    private T tmpB;
    private T tmpC;

    public LinePath(Array<T> waypoints) {
        this(waypoints, false);
    }

    public LinePath(Array<T> waypoints, boolean isOpen) {
        this.isOpen = isOpen;
        this.createPath(waypoints);
        this.nearestPointOnCurrentSegment = ((Vector)waypoints.first()).cpy();
        this.nearestPointOnPath = ((Vector)waypoints.first()).cpy();
        this.tmpB = ((Vector)waypoints.first()).cpy();
        this.tmpC = ((Vector)waypoints.first()).cpy();
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public float getLength() {
        return this.pathLength;
    }

    @Override
    public T getStartPoint() {
        return this.segments.first().begin;
    }

    @Override
    public T getEndPoint() {
        return this.segments.peek().end;
    }

    public float calculatePointSegmentSquareDistance(T out, T a, T b, T c) {
        out.set(a);
        this.tmpB.set(b);
        this.tmpC.set(c);
        T ab = this.tmpB.sub(a);
        float abLen2 = ab.len2();
        if (abLen2 != 0.0f) {
            float t = this.tmpC.sub(a).dot(ab) / abLen2;
            out.mulAdd(ab, (float)MathUtils.clamp(t, 0.0f, 1.0f));
        }
        return out.dst2(c);
    }

    @Override
    public LinePathParam createParam() {
        return new LinePathParam();
    }

    @Override
    public float calculateDistance(T agentCurrPos, LinePathParam parameter) {
        float smallestDistance2 = Float.POSITIVE_INFINITY;
        Segment<T> nearestSegment = null;
        for (int i = 0; i < this.segments.size; ++i) {
            Segment<T> segment = this.segments.get(i);
            float distance2 = this.calculatePointSegmentSquareDistance(this.nearestPointOnCurrentSegment, segment.begin, segment.end, agentCurrPos);
            if (distance2 >= smallestDistance2) continue;
            this.nearestPointOnPath.set(this.nearestPointOnCurrentSegment);
            smallestDistance2 = distance2;
            nearestSegment = segment;
            parameter.segmentIndex = i;
        }
        float lengthOnPath = nearestSegment.cumulativeLength - this.nearestPointOnPath.dst(nearestSegment.end);
        parameter.setDistance(lengthOnPath);
        return lengthOnPath;
    }

    @Override
    public void calculateTargetPosition(T out, LinePathParam param, float targetDistance) {
        if (this.isOpen) {
            if (targetDistance < 0.0f) {
                targetDistance = 0.0f;
            } else if (targetDistance > this.pathLength) {
                targetDistance = this.pathLength;
            }
        } else if (targetDistance < 0.0f) {
            targetDistance = this.pathLength + targetDistance % this.pathLength;
        } else if (targetDistance > this.pathLength) {
            targetDistance %= this.pathLength;
        }
        Segment<T> desiredSegment = null;
        for (int i = 0; i < this.segments.size; ++i) {
            Segment<T> segment = this.segments.get(i);
            if (segment.cumulativeLength < targetDistance) continue;
            desiredSegment = segment;
            break;
        }
        float distance = desiredSegment.cumulativeLength - targetDistance;
        out.set(desiredSegment.begin).sub(desiredSegment.end).scl((float)(distance / desiredSegment.length)).add(desiredSegment.end);
    }

    public void createPath(Array<T> waypoints) {
        if (waypoints == null || waypoints.size < 2) {
            throw new IllegalArgumentException("waypoints cannot be null and must contain at least two (2) waypoints");
        }
        this.segments = new Array(waypoints.size);
        this.pathLength = 0.0f;
        Vector curr = (Vector)waypoints.first();
        Vector prev = null;
        for (int i = 1; i <= waypoints.size; ++i) {
            prev = curr;
            if (i < waypoints.size) {
                curr = (Vector)waypoints.get(i);
            } else {
                if (this.isOpen) break;
                curr = (Vector)waypoints.first();
            }
            Segment<Vector> segment = new Segment<Vector>(prev, curr);
            this.pathLength += segment.length;
            segment.cumulativeLength = this.pathLength;
            this.segments.add(segment);
        }
    }

    public Array<Segment<T>> getSegments() {
        return this.segments;
    }

    public static class Segment<T extends Vector<T>> {
        T begin;
        T end;
        float length;
        float cumulativeLength;

        Segment(T begin, T end) {
            this.begin = begin;
            this.end = end;
            this.length = begin.dst(end);
        }

        public T getBegin() {
            return this.begin;
        }

        public T getEnd() {
            return this.end;
        }

        public float getLength() {
            return this.length;
        }

        public float getCumulativeLength() {
            return this.cumulativeLength;
        }
    }

    public static class LinePathParam
    implements Path.PathParam {
        int segmentIndex;
        float distance;

        @Override
        public float getDistance() {
            return this.distance;
        }

        @Override
        public void setDistance(float distance) {
            this.distance = distance;
        }

        public int getSegmentIndex() {
            return this.segmentIndex;
        }
    }

}

