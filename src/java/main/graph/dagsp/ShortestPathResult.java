package graph.dagsp;

import graph.metrics.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShortestPathResult {
    private final int source;
    private final int[] distances;
    private final int[] predecessors;
    private final Metrics metrics;

    public ShortestPathResult(int source, int[] distances, int[] predecessors, Metrics metrics) {
        this.source = source;
        this.distances = distances;
        this.predecessors = predecessors;
        this.metrics = metrics;
    }

    public int[] getDistances() { return distances; }
    public int[] getPredecessors() { return predecessors; }
    public Metrics getMetrics() { return metrics; }

    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (predecessors[target] == -1 && target != source) {
            return path;
        }

        for (int at = target; at != -1; at = predecessors[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public void printResults() {
        System.out.println("Shortest paths from source " + source + ":");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("To " + i + ": " +
                    (distances[i] == Integer.MAX_VALUE ? "INF" : distances[i]));
        }
        System.out.println("Metrics: " + metrics.getElapsedTime() + " ns");
    }
}