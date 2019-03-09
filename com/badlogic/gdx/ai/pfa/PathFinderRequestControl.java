/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.utils.TimeUtils;

public class PathFinderRequestControl<N> {
    public static final boolean DEBUG = false;
    Telegraph server;
    PathFinder<N> pathFinder;
    long lastTime;
    long timeToRun;
    long timeTolerance;

    public boolean execute(PathFinderRequest<N> request) {
        ++request.executionFrames;
        do {
            long currentTime;
            if (request.status == 0) {
                currentTime = TimeUtils.nanoTime();
                this.timeToRun -= currentTime - this.lastTime;
                if (this.timeToRun <= this.timeTolerance) {
                    return false;
                }
                if (!request.initializeSearch(this.timeToRun)) {
                    return false;
                }
                request.changeStatus(1);
                this.lastTime = currentTime;
            }
            if (request.status == 1) {
                currentTime = TimeUtils.nanoTime();
                this.timeToRun -= currentTime - this.lastTime;
                if (this.timeToRun <= this.timeTolerance) {
                    return false;
                }
                if (!request.search(this.pathFinder, this.timeToRun)) {
                    return false;
                }
                request.changeStatus(2);
                this.lastTime = currentTime;
            }
            if (request.status != 2) break;
            currentTime = TimeUtils.nanoTime();
            this.timeToRun -= currentTime - this.lastTime;
            if (this.timeToRun <= this.timeTolerance) {
                return false;
            }
            if (!request.finalizeSearch(this.timeToRun)) {
                return false;
            }
            request.changeStatus(3);
            if (this.server != null) {
                MessageDispatcher dispatcher = request.dispatcher != null ? request.dispatcher : MessageManager.getInstance();
                dispatcher.dispatchMessage(this.server, request.client, request.responseMessageCode, request);
            }
            this.lastTime = currentTime;
        } while (request.statusChanged && request.status == 0);
        return true;
    }
}

