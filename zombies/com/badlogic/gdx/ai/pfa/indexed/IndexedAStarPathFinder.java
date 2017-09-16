/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.pfa.indexed;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.TimeUtils;

public class IndexedAStarPathFinder<N>
implements PathFinder<N> {
    IndexedGraph<N> graph;
    NodeRecord<N>[] nodeRecords;
    BinaryHeap<NodeRecord<N>> openList;
    NodeRecord<N> current;
    public Metrics metrics;
    private int searchId;
    private static final int UNVISITED = 0;
    private static final int OPEN = 1;
    private static final int CLOSED = 2;

    public IndexedAStarPathFinder(IndexedGraph<N> graph) {
        this(graph, false);
    }

    public IndexedAStarPathFinder(IndexedGraph<N> graph, boolean calculateMetrics) {
        this.graph = graph;
        this.nodeRecords = new NodeRecord[graph.getNodeCount()];
        this.openList = new BinaryHeap();
        if (calculateMetrics) {
            this.metrics = new Metrics();
        }
    }

    @Override
    public boolean searchConnectionPath(N startNode, N endNode, Heuristic<N> heuristic, GraphPath<Connection<N>> outPath) {
        this.search(startNode, endNode, heuristic);
        if (this.current.node != endNode) {
            return false;
        }
        this.generateConnectionPath(startNode, outPath);
        return true;
    }

    @Override
    public boolean searchNodePath(N startNode, N endNode, Heuristic<N> heuristic, GraphPath<N> outPath) {
        this.search(startNode, endNode, heuristic);
        if (this.current.node != endNode) {
            return false;
        }
        this.generateNodePath(startNode, outPath);
        return true;
    }

    protected void search(N startNode, N endNode, Heuristic<N> heuristic) {
        this.initSearch(startNode, endNode, heuristic);
        do {
            this.current = this.openList.pop();
            this.current.category = 2;
            if (this.current.node == endNode) {
                return;
            }
            this.visitChildren(endNode, heuristic);
        } while (this.openList.size > 0);
    }

    @Override
    public boolean search(PathFinderRequest<N> request, long timeToRun) {
        long lastTime = TimeUtils.nanoTime();
        if (request.statusChanged) {
            this.initSearch(request.startNode, request.endNode, request.heuristic);
            request.statusChanged = false;
        }
        do {
            long currentTime;
            if ((timeToRun -= (currentTime = TimeUtils.nanoTime()) - lastTime) <= 100) {
                return false;
            }
            this.current = this.openList.pop();
            this.current.category = 2;
            if (this.current.node == request.endNode) {
                request.pathFound = true;
                this.generateNodePath(request.startNode, request.resultPath);
                return true;
            }
            this.visitChildren(request.endNode, request.heuristic);
            lastTime = currentTime;
        } while (this.openList.size > 0);
        request.pathFound = false;
        return true;
    }

    protected void initSearch(N startNode, N endNode, Heuristic<N> heuristic) {
        if (this.metrics != null) {
            this.metrics.reset();
        }
        if (++this.searchId < 0) {
            this.searchId = 1;
        }
        this.openList.clear();
        NodeRecord<N> startRecord = this.getNodeRecord(startNode);
        startRecord.node = startNode;
        startRecord.connection = null;
        startRecord.costSoFar = 0.0f;
        this.addToOpenList(startRecord, heuristic.estimate(startNode, endNode));
        this.current = null;
    }

    protected void visitChildren(N endNode, Heuristic<N> heuristic) {
        Array<Connection<N>> connections = this.graph.getConnections(this.current.node);
        for (int i = 0; i < connections.size; ++i) {
            float nodeHeuristic;
            if (this.metrics != null) {
                ++this.metrics.visitedNodes;
            }
            Connection<N> connection = connections.get(i);
            N node = connection.getToNode();
            float nodeCost = this.current.costSoFar + connection.getCost();
            NodeRecord<N> nodeRecord = this.getNodeRecord(node);
            if (nodeRecord.category == 2) {
                if (nodeRecord.costSoFar <= nodeCost) continue;
                nodeHeuristic = nodeRecord.getEstimatedTotalCost() - nodeRecord.costSoFar;
            } else if (nodeRecord.category == 1) {
                if (nodeRecord.costSoFar <= nodeCost) continue;
                this.openList.remove(nodeRecord);
                nodeHeuristic = nodeRecord.getEstimatedTotalCost() - nodeRecord.costSoFar;
            } else {
                nodeHeuristic = heuristic.estimate(node, endNode);
            }
            nodeRecord.costSoFar = nodeCost;
            nodeRecord.connection = connection;
            this.addToOpenList(nodeRecord, nodeCost + nodeHeuristic);
        }
    }

    protected void generateConnectionPath(N startNode, GraphPath<Connection<N>> outPath) {
        while (this.current.node != startNode) {
            outPath.add(this.current.connection);
            this.current = this.nodeRecords[this.graph.getIndex(this.current.connection.getFromNode())];
        }
        outPath.reverse();
    }

    protected void generateNodePath(N startNode, GraphPath<N> outPath) {
        while (this.current.connection != null) {
            outPath.add(this.current.node);
            this.current = this.nodeRecords[this.graph.getIndex(this.current.connection.getFromNode())];
        }
        outPath.add(startNode);
        outPath.reverse();
    }

    protected void addToOpenList(NodeRecord<N> nodeRecord, float estimatedTotalCost) {
        this.openList.add(nodeRecord, estimatedTotalCost);
        nodeRecord.category = 1;
        if (this.metrics != null) {
            ++this.metrics.openListAdditions;
            this.metrics.openListPeak = Math.max(this.metrics.openListPeak, this.openList.size);
        }
    }

    protected NodeRecord<N> getNodeRecord(N node) {
        int index = this.graph.getIndex(node);
        NodeRecord<N> nr = this.nodeRecords[index];
        if (nr != null) {
            if (nr.searchId != this.searchId) {
                nr.category = 0;
                nr.searchId = this.searchId;
            }
            return nr;
        }
        nr = this.nodeRecords[index] = new NodeRecord();
        nr.node = node;
        nr.searchId = this.searchId;
        return nr;
    }

    public static class Metrics {
        public int visitedNodes;
        public int openListAdditions;
        public int openListPeak;

        public void reset() {
            this.visitedNodes = 0;
            this.openListAdditions = 0;
            this.openListPeak = 0;
        }
    }

    static class NodeRecord<N>
    extends BinaryHeap.Node {
        N node;
        Connection<N> connection;
        float costSoFar;
        int category;
        int searchId;

        public NodeRecord() {
            super(0.0f);
        }

        public float getEstimatedTotalCost() {
            return this.getValue();
        }
    }

}

