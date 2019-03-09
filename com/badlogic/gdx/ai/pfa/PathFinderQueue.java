/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.ai.pfa.PathFinderRequestControl;
import com.badlogic.gdx.ai.sched.Schedulable;
import com.badlogic.gdx.ai.utils.CircularBuffer;
import com.badlogic.gdx.utils.TimeUtils;

public class PathFinderQueue<N>
implements Schedulable,
Telegraph {
    public static final long TIME_TOLERANCE = 100;
    CircularBuffer<PathFinderRequest<N>> requestQueue;
    PathFinder<N> pathFinder;
    PathFinderRequest<N> currentRequest;
    PathFinderRequestControl<N> requestControl;

    public PathFinderQueue(PathFinder<N> pathFinder) {
        this.pathFinder = pathFinder;
        this.requestQueue = new CircularBuffer(16);
        this.currentRequest = null;
        this.requestControl = new PathFinderRequestControl();
    }

    @Override
    public void run(long timeToRun) {
        this.requestControl.lastTime = TimeUtils.nanoTime();
        this.requestControl.timeToRun = timeToRun;
        this.requestControl.timeTolerance = 100;
        this.requestControl.pathFinder = this.pathFinder;
        this.requestControl.server = this;
        if (this.currentRequest == null) {
            this.currentRequest = this.requestQueue.read();
        }
        while (this.currentRequest != null) {
            boolean finished = this.requestControl.execute(this.currentRequest);
            if (!finished) {
                return;
            }
            this.currentRequest = this.requestQueue.read();
        }
    }

    @Override
    public boolean handleMessage(Telegram telegram) {
        PathFinderRequest pfr = (PathFinderRequest)telegram.extraInfo;
        pfr.client = telegram.sender;
        pfr.status = 0;
        pfr.statusChanged = true;
        pfr.executionFrames = 0;
        this.requestQueue.store(pfr);
        return true;
    }

    public int size() {
        return this.requestQueue.size();
    }
}

