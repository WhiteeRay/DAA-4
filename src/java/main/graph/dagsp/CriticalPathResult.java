package graph.dagsp;

import graph.metrics.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CriticalPathResult {
    private final int[] longestDistances;
    private final int[] predecessors;
    private final Metrics metrics;

    public CriticalPathResult(int[] longestDistances, int[] predecessors, Metrics metrics) {
        this.longestDistances = longestDistances;
        this.predecessors = predecessors;
        this.metrics = metrics;
    }

    public int getCriticalPathLength() {
        int max = Integer.MIN_VALUE;
        for (int dist : longestDistances) {
            if (dist > max) max = dist;
        }
        return max;
    }

    public List<Integer> getCriticalPath() {
        int maxDist = Integer.MIN_VALUE;
        int endNode = -1;

        for (int i = 0; i < longestDistances.length; i++) {
            if (longestDistances[i] > maxDist) {
                maxDist = longestDistances[i];
                endNode = i;
            }
        }

        if (endNode == -1) return new ArrayList<>();

        List<Integer> path = new ArrayList<>();
        for (int at = endNode; at != -1; at = predecessors[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public void printResults() {
        System.out.println("Critical Path Analysis:");
        System.out.println("Critical Path Length: " + getCriticalPathLength());
        System.out.println("Critical Path: " + getCriticalPath());
        System.out.println("Metrics: " + metrics.getElapsedTime() + " ns");
    }
}