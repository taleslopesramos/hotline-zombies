/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;

public class PathFinderRequest<N> {
    public static final int SEARCH_NEW = 0;
    public static final int SEARCH_INITIALIZED = 1;
    public static final int SEARCH_DONE = 2;
    public static final int SEARCH_FINALIZED = 3;
    public N startNode;
    public N endNode;
    public Heuristic<N> heuristic;
    public GraphPath<N> resultPath;
    public int executionFrames;
    public boolean pathFound;
    public int status;
    public boolean statusChanged;
    public Telegraph client;
    public int responseMessageCode;
    public MessageDispatcher dispatcher;

    public PathFinderRequest() {
    }

    public PathFinderRequest(N startNode, N endNode, Heuristic<N> heuristic, GraphPath<N> resultPath) {
        this(startNode, endNode, heuristic, resultPath, MessageManager.getInstance());
    }

    public PathFinderRequest(N startNode, N endNode, Heuristic<N> heuristic, GraphPath<N> resultPath, MessageDispatcher dispatcher) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.heuristic = heuristic;
        this.resultPath = resultPath;
        this.dispatcher = dispatcher;
        this.executionFrames = 0;
        this.pathFound = false;
        this.status = 0;
        this.statusChanged = false;
    }

    public void changeStatus(int newStatus) {
        this.status = newStatus;
        this.statusChanged = true;
    }

    public boolean initializeSearch(long timeToRun) {
        return true;
    }

    public boolean search(PathFinder<N> pathFinder, long timeToRun) {
        return pathFinder.search(this, timeToRun);
    }

    public boolean finalizeSearch(long timeToRun) {
        return true;
    }
}

