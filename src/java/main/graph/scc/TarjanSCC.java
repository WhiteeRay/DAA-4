package graph.scc;

import graph.metrics.GraphMetrics;
import graph.metrics.Metrics;
import graph.models.Edge;
import graph.models.Graph;

import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    private int index;
    private int[] indices;
    private int[] lowLinks;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> components;

    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new GraphMetrics();
    }

    public SCCResult findSCCs() {
        metrics.startTimer();
        metrics.reset();

        int n = graph.getVertices();
        indices = new int[n];
        lowLinks = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        components = new ArrayList<>();

        Arrays.fill(indices, -1);

        for (int i = 0; i < n; i++) {
            metrics.incrementVisit();
            if (indices[i] == -1) {
                strongConnect(i);
            }
        }

        metrics.stopTimer();
        return new SCCResult(components, metrics);
    }

    private void strongConnect(int v) {
        metrics.incrementOperation("DFS_CALL");
        indices[v] = index;
        lowLinks[v] = index;
        index++;
        stack.push(v);
        onStack[v] = true;

        for (Edge edge : graph.getEdges(v)) {
            metrics.incrementEdgeTraversal();
            int w = edge.getTo();

            if (indices[w] == -1) {
                strongConnect(w);
                lowLinks[v] = Math.min(lowLinks[v], lowLinks[w]);
            } else if (onStack[w]) {
                lowLinks[v] = Math.min(lowLinks[v], indices[w]);
            }
        }

        if (lowLinks[v] == indices[v]) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                component.add(w);
            } while (w != v);
            components.add(component);
        }
    }
}