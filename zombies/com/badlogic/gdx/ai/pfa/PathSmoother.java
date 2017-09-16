/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.pfa.PathSmootherRequest;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.TimeUtils;

public class PathSmoother<N, V extends Vector<V>> {
    RaycastCollisionDetector<V> raycastCollisionDetector;
    Ray<V> ray;

    public PathSmoother(RaycastCollisionDetector<V> raycastCollisionDetector) {
        this.raycastCollisionDetector = raycastCollisionDetector;
    }

    public int smoothPath(SmoothableGraphPath<N, V> path) {
        int inputIndex;
        int inputPathLength = path.getCount();
        if (inputPathLength <= 2) {
            return 0;
        }
        if (this.ray == null) {
            V vec = path.getNodePosition(0);
            this.ray = new Ray(vec.cpy(), vec.cpy());
        }
        int outputIndex = 1;
        boolean collided = false;
        for (inputIndex = 2; inputIndex < inputPathLength; ++inputIndex) {
            this.ray.start.set(path.getNodePosition(outputIndex - 1));
            this.ray.end.set(path.getNodePosition(inputIndex));
            collided = this.raycastCollisionDetector.collides(this.ray);
            if (!collided) continue;
            path.swapNodes(outputIndex, inputIndex - 1);
            ++outputIndex;
        }
        path.swapNodes(outputIndex, inputIndex - 1);
        path.truncatePath(outputIndex + 1);
        return inputIndex - outputIndex - 1;
    }

    public boolean smoothPath(PathSmootherRequest<N, V> request, long timeToRun) {
        long lastTime = TimeUtils.nanoTime();
        SmoothableGraphPath path = request.path;
        int inputPathLength = path.getCount();
        if (inputPathLength <= 2) {
            return true;
        }
        if (request.isNew) {
            request.isNew = false;
            if (this.ray == null) {
                Object vec = request.path.getNodePosition(0);
                this.ray = new Ray(vec.cpy(), vec.cpy());
            }
            request.outputIndex = 1;
            request.inputIndex = 2;
        }
        while (request.inputIndex < inputPathLength) {
            long currentTime = TimeUtils.nanoTime();
            if ((timeToRun -= currentTime - lastTime) <= 100) {
                return false;
            }
            this.ray.start.set(path.getNodePosition(request.outputIndex - 1));
            this.ray.end.set(path.getNodePosition(request.inputIndex));
            boolean collided = this.raycastCollisionDetector.collides(this.ray);
            if (collided) {
                path.swapNodes(request.outputIndex, request.inputIndex - 1);
                ++request.outputIndex;
            }
            ++request.inputIndex;
            lastTime = currentTime;
        }
        path.swapNodes(request.outputIndex, request.inputIndex - 1);
        path.truncatePath(request.outputIndex + 1);
        return true;
    }
}

