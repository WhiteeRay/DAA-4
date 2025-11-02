package graph.scc;

import graph.metrics.Metrics;

import java.util.List;

public class SCCResult {
    private final List<List<Integer>> components;
    private final Metrics metrics;

    public SCCResult(List<List<Integer>> components, Metrics metrics) {
        this.components = components;
        this.metrics = metrics;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void printResults() {
        System.out.println("Strongly Connected Components:");
        for (int i = 0; i < components.size(); i++) {
            System.out.println("Component " + i + ": " + components.get(i));
        }
        System.out.println("Total components: " + components.size());
        System.out.println("Metrics: " + metrics.getElapsedTime() + " ns, " +
                metrics.getTotalVisits() + " visits");
    }
}