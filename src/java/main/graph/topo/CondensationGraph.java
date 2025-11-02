package graph.topo;

import graph.models.Edge;
import graph.models.Graph;
import graph.scc.SCCResult;

import java.util.*;

public class CondensationGraph {
    private final Graph originalGraph;
    private final SCCResult sccResult;
    private Graph condensationGraph;
    private Map<Integer, Integer> nodeToComponent;
    private List<Integer> topologicalOrder;

    public CondensationGraph(Graph originalGraph, SCCResult sccResult) {
        this.originalGraph = originalGraph;
        this.sccResult = sccResult;
        buildCondensationGraph();
    }

    private void buildCondensationGraph() {
        List<List<Integer>> components = sccResult.getComponents();
        int componentCount = components.size();

        // Map each node to its component
        nodeToComponent = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int node : components.get(i)) {
                nodeToComponent.put(node, i);
            }
        }

        // Build condensation graph
        condensationGraph = new Graph(componentCount);
        Set<String> edgesAdded = new HashSet<>();

        for (int u = 0; u < originalGraph.getVertices(); u++) {
            int compU = nodeToComponent.get(u);

            for (Edge edge : originalGraph.getEdges(u)) {
                int v = edge.getTo();
                int compV = nodeToComponent.get(v);

                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;
                    if (!edgesAdded.contains(edgeKey)) {
                        condensationGraph.addEdge(compU, compV, 1);
                        edgesAdded.add(edgeKey);
                    }
                }
            }
        }
    }

    public Graph getCondensationGraph() {
        return condensationGraph;
    }

    public Map<Integer, Integer> getNodeToComponent() {
        return nodeToComponent;
    }

    public List<Integer> getTopologicalOrder() {
        if (topologicalOrder == null) {
            topologicalOrder = new TopologicalSort(condensationGraph).getTopologicalOrder();
        }
        return topologicalOrder;
    }
}

