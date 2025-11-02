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

        nodeToComponent = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int node : components.get(i)) {
                nodeToComponent.put(node, i);
            }
        }

        condensationGraph = new Graph(componentCount);
        Map<String, Integer> edgeWeights = new HashMap<>();

        for (int u = 0; u < originalGraph.getVertices(); u++) {
            int compU = nodeToComponent.get(u);

            for (Edge edge : originalGraph.getEdges(u)) {
                int v = edge.getTo();
                int compV = nodeToComponent.get(v);

                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;

                    edgeWeights.put(edgeKey, edge.getWeight());
                }
            }
        }

        for (Map.Entry<String, Integer> entry : edgeWeights.entrySet()) {
            String[] parts = entry.getKey().split("->");
            int fromComp = Integer.parseInt(parts[0]);
            int toComp = Integer.parseInt(parts[1]);
            condensationGraph.addEdge(fromComp, toComp, entry.getValue());
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

