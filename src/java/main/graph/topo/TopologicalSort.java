package graph.topo;

import graph.metrics.GraphMetrics;
import graph.metrics.Metrics;
import graph.models.Edge;
import graph.models.Graph;

import java.util.*;

public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public TopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new GraphMetrics();
    }

    public List<Integer> getTopologicalOrder() {
        metrics.startTimer();
        metrics.reset();

        int n = graph.getVertices();
        int[] inDegree = new int[n];

        // Calculate in-degrees
        for (int i = 0; i < n; i++) {
            metrics.incrementVisit();
            for (Edge edge : graph.getEdges(i)) {
                metrics.incrementEdgeTraversal();
                inDegree[edge.getTo()]++;
            }
        }

        // Kahn's algorithm
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementOperation("QUEUE_PUSH");
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            metrics.incrementOperation("QUEUE_POP");
            topologicalOrder.add(node);

            for (Edge edge : graph.getEdges(node)) {
                metrics.incrementEdgeTraversal();
                int neighbor = edge.getTo();
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                    metrics.incrementOperation("QUEUE_PUSH");
                }
            }
        }

        metrics.stopTimer();
        return topologicalOrder;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}