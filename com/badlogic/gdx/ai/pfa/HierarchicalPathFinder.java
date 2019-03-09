/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.HierarchicalGraph;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.ai.pfa.PathFinderRequestControl;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.PrintStream;

public class HierarchicalPathFinder<N>
implements PathFinder<N> {
    public static boolean DEBUG = false;
    HierarchicalGraph<N> graph;
    PathFinder<N> levelPathFinder;
    LevelPathFinderRequest<N> levelRequest;
    PathFinderRequestControl<N> levelRequestControl;

    public HierarchicalPathFinder(HierarchicalGraph<N> graph, PathFinder<N> levelPathFinder) {
        this.graph = graph;
        this.levelPathFinder = levelPathFinder;
        this.levelRequest = null;
        this.levelRequestControl = null;
    }

    @Override
    public boolean searchNodePath(N startNode, N endNode, Heuristic<N> heuristic, GraphPath<N> outPath) {
        if (startNode == endNode) {
            return true;
        }
        N currentStartNode = startNode;
        N currentEndNode = endNode;
        int levelOfNodes = 0;
        int currentLevel = this.graph.getLevelCount() - 1;
        while (currentLevel >= 0) {
            N currentEndNodeParent;
            currentStartNode = this.graph.convertNodeBetweenLevels(0, startNode, currentLevel);
            currentEndNode = this.graph.convertNodeBetweenLevels(levelOfNodes, currentEndNode, currentLevel);
            if (currentLevel == 0 && (currentEndNodeParent = this.graph.convertNodeBetweenLevels(0, currentEndNode, 1)) == this.graph.convertNodeBetweenLevels(0, endNode, 1) && currentEndNodeParent == this.graph.convertNodeBetweenLevels(0, startNode, 1)) {
                currentEndNode = endNode;
            }
            levelOfNodes = currentLevel--;
            if (currentStartNode == currentEndNode) continue;
            this.graph.setLevel(levelOfNodes);
            outPath.clear();
            boolean pathFound = this.levelPathFinder.searchNodePath(currentStartNode, currentEndNode, heuristic, outPath);
            if (!pathFound) {
                return false;
            }
            currentEndNode = outPath.get(1);
        }
        return true;
    }

    @Override
    public boolean searchConnectionPath(N startNode, N endNode, Heuristic<N> heuristic, GraphPath<Connection<N>> outPath) {
        if (startNode == endNode) {
            return true;
        }
        N currentStartNode = startNode;
        N currentEndNode = endNode;
        int levelOfNodes = 0;
        int currentLevel = this.graph.getLevelCount() - 1;
        while (currentLevel >= 0) {
            N currentEndNodeParent;
            currentStartNode = this.graph.convertNodeBetweenLevels(0, startNode, currentLevel);
            currentEndNode = this.graph.convertNodeBetweenLevels(levelOfNodes, currentEndNode, currentLevel);
            if (currentLevel == 0 && (currentEndNodeParent = this.graph.convertNodeBetweenLevels(0, currentEndNode, 1)) == this.graph.convertNodeBetweenLevels(0, endNode, 1) && currentEndNodeParent == this.graph.convertNodeBetweenLevels(0, startNode, 1)) {
                currentEndNode = endNode;
            }
            levelOfNodes = currentLevel--;
            if (currentStartNode == currentEndNode) continue;
            this.graph.setLevel(levelOfNodes);
            outPath.clear();
            boolean pathFound = this.levelPathFinder.searchConnectionPath(currentStartNode, currentEndNode, heuristic, outPath);
            if (!pathFound) {
                return false;
            }
            currentEndNode = outPath.get(0).getToNode();
        }
        return true;
    }

    @Override
    public boolean search(PathFinderRequest<N> request, long timeToRun) {
        if (DEBUG) {
            System.out.println("Enter interruptible HPF; request.status = " + request.status);
        }
        if (this.levelRequest == null) {
            this.levelRequest = new LevelPathFinderRequest();
            this.levelRequestControl = new PathFinderRequestControl();
        }
        if (request.statusChanged) {
            if (DEBUG) {
                System.out.println("-- statusChanged");
            }
            if (request.startNode == request.endNode) {
                return true;
            }
            this.levelRequestControl.lastTime = TimeUtils.nanoTime();
            this.levelRequestControl.timeToRun = timeToRun;
            this.levelRequestControl.timeTolerance = 100;
            this.levelRequestControl.server = null;
            this.levelRequestControl.pathFinder = this.levelPathFinder;
            this.levelRequest.hpf = this;
            this.levelRequest.hpfRequest = request;
            this.levelRequest.status = 0;
            this.levelRequest.statusChanged = true;
            this.levelRequest.heuristic = request.heuristic;
            this.levelRequest.resultPath = request.resultPath;
            this.levelRequest.startNode = request.startNode;
            this.levelRequest.endNode = request.endNode;
            this.levelRequest.levelOfNodes = 0;
            this.levelRequest.currentLevel = this.graph.getLevelCount() - 1;
        }
        while (this.levelRequest.currentLevel >= 0) {
            boolean finished = this.levelRequestControl.execute(this.levelRequest);
            if (!finished) {
                return false;
            }
            this.levelRequest.executionFrames = 0;
            this.levelRequest.status = 0;
            this.levelRequest.statusChanged = true;
            if (this.levelRequest.pathFound) continue;
            return true;
        }
        if (DEBUG) {
            System.out.println("-- before exit");
        }
        return true;
    }

    static class LevelPathFinderRequest<N>
    extends PathFinderRequest<N> {
        HierarchicalPathFinder<N> hpf;
        PathFinderRequest<N> hpfRequest;
        int levelOfNodes;
        int currentLevel;

        LevelPathFinderRequest() {
        }

        @Override
        public boolean initializeSearch(long timeToRun) {
            this.executionFrames = 0;
            this.pathFound = false;
            this.status = 0;
            this.statusChanged = false;
            do {
                Object currentEndNodeParent;
                this.startNode = this.hpf.graph.convertNodeBetweenLevels(0, this.hpfRequest.startNode, this.currentLevel);
                this.endNode = this.hpf.graph.convertNodeBetweenLevels(this.levelOfNodes, this.endNode, this.currentLevel);
                if (this.currentLevel == 0 && (currentEndNodeParent = this.hpf.graph.convertNodeBetweenLevels(0, this.endNode, 1)) == this.hpf.graph.convertNodeBetweenLevels(0, this.hpfRequest.endNode, 1) && currentEndNodeParent == this.hpf.graph.convertNodeBetweenLevels(0, this.hpfRequest.startNode, 1)) {
                    this.endNode = this.hpfRequest.endNode;
                }
                if (HierarchicalPathFinder.DEBUG) {
                    System.out.println("LevelPathFinder initializeSearch");
                }
                this.levelOfNodes = this.currentLevel--;
            } while (this.startNode == this.endNode && this.currentLevel >= 0);
            this.hpf.graph.setLevel(this.levelOfNodes);
            this.resultPath.clear();
            return true;
        }

        @Override
        public boolean search(PathFinder<N> pathFinder, long timeToRun) {
            if (HierarchicalPathFinder.DEBUG) {
                System.out.println("LevelPathFinder search; status: " + this.status);
            }
            return super.search(pathFinder, timeToRun);
        }

        @Override
        public boolean finalizeSearch(long timeToRun) {
            this.hpfRequest.pathFound = this.pathFound;
            if (this.pathFound) {
                this.endNode = this.resultPath.get(1);
            }
            if (HierarchicalPathFinder.DEBUG) {
                System.out.println("LevelPathFinder finalizeSearch; status: " + this.status);
            }
            return true;
        }
    }

}

