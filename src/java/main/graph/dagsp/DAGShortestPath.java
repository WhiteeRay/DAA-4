package graph.dagsp;

import graph.metrics.GraphMetrics;
import graph.metrics.Metrics;
import graph.models.Edge;
import graph.models.Graph;
import graph.topo.TopologicalSort;

import java.util.*;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    private Object invertedResult;

    public DAGShortestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new GraphMetrics();
    }

    public ShortestPathResult findShortestPaths(int source) {
        metrics.startTimer();
        metrics.reset();

        int n = graph.getVertices();
        int[] dist = new int[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;


        TopologicalSort topo = new TopologicalSort(graph);
        List<Integer> order = topo.getTopologicalOrder();


        for (int u : order) {
            metrics.incrementVisit();
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge edge : graph.getEdges(u)) {
                    metrics.incrementEdgeTraversal();
                    metrics.incrementOperation("RELAX");
                    int v = edge.getTo();
                    int newDist = dist[u] + edge.getWeight();
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                    }
                }
            }
        }

        metrics.stopTimer();
        return new ShortestPathResult(source, dist, prev, metrics);
    }

    public CriticalPathResult findCriticalPath() {

        Graph invertedGraph = invertWeights();
        DAGShortestPath invertedSP = new DAGShortestPath(invertedGraph);


        int source = findSourceNode();
        ShortestPathResult invertedResult = invertedSP.findShortestPaths(source);


        int[] longestDist = new int[graph.getVertices()];
        for (int i = 0; i < longestDist.length; i++) {
            longestDist[i] = -invertedResult.getDistances()[i];
        }

        return new CriticalPathResult(longestDist, invertedResult.getPredecessors(),
                invertedResult.getMetrics());
    }

    private Graph invertWeights() {
        Graph inverted = new Graph(graph.getVertices());
        for (int i = 0; i < graph.getVertices(); i++) {
            for (Edge edge : graph.getEdges(i)) {
                inverted.addEdge(edge.getFrom(), edge.getTo(), -edge.getWeight());
            }
        }
        return inverted;
    }

    private int findSourceNode() {

        int[] inDegree = new int[graph.getVertices()];
        for (int i = 0; i < graph.getVertices(); i++) {
            for (Edge edge : graph.getEdges(i)) {
                inDegree[edge.getTo()]++;
            }
        }

        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) return i;
        }
        return 0;
    }
}
