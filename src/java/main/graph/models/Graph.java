package graph.models;

import java.util.*;

public class Graph {
    private final Map<Integer, List<Edge>> adjacencyList;
    private final Map<Integer, Integer> nodeDurations;
    private final int vertices;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new HashMap<>();
        this.nodeDurations = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
            nodeDurations.put(i, 0); // default duration
        }
    }

    public void addEdge(int from, int to, int weight) {
        adjacencyList.get(from).add(new Edge(from, to, weight));
    }

    public void setNodeDuration(int node, int duration) {
        nodeDurations.put(node, duration);
    }

    public List<Edge> getEdges(int node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    public int getNodeDuration(int node) {
        return nodeDurations.get(node);
    }

    public int getVertices() {
        return vertices;
    }

    public Map<Integer, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}
