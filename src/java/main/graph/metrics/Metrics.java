package graph.metrics;

public interface Metrics {
    void incrementOperation(String operation);
    void incrementVisit();
    void incrementEdgeTraversal();
    long getOperationCount(String operation);
    long getTotalVisits();
    long getTotalEdgeTraversals();
    long getElapsedTime();
    void startTimer();
    void stopTimer();
    void reset();
}