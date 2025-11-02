package graph.metrics;

import java.util.concurrent.ConcurrentHashMap;

public class GraphMetrics implements Metrics {
    private final ConcurrentHashMap<String, Long> operationCounts;
    private long startTime;
    private long endTime;
    private long visits;
    private long edgeTraversals;

    public GraphMetrics() {
        this.operationCounts = new ConcurrentHashMap<>();
        reset();
    }

    @Override
    public void incrementOperation(String operation) {
        operationCounts.merge(operation, 1L, Long::sum);
    }

    @Override
    public void incrementVisit() {
        visits++;
    }

    @Override
    public void incrementEdgeTraversal() {
        edgeTraversals++;
    }

    @Override
    public long getOperationCount(String operation) {
        return operationCounts.getOrDefault(operation, 0L);
    }

    @Override
    public long getTotalVisits() {
        return visits;
    }

    @Override
    public long getTotalEdgeTraversals() {
        return edgeTraversals;
    }

    @Override
    public long getElapsedTime() {
        return endTime - startTime;
    }

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        endTime = System.nanoTime();
    }

    @Override
    public void reset() {
        operationCounts.clear();
        visits = 0;
        edgeTraversals = 0;
        startTime = 0;
        endTime = 0;
    }
}