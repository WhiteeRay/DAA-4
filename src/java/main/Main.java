import graph.GraphReader;
import graph.dagsp.CriticalPathResult;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.ShortestPathResult;
import graph.models.Edge;
import graph.models.Graph;
import graph.scc.SCCResult;
import graph.scc.TarjanSCC;
import graph.topo.CondensationGraph;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inputFile = "data/tasks.json";

        try {
            processJsonFile(inputFile);
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    private static void processJsonFile(String filename) throws IOException {
        System.out.println("\nProcessing file: " + filename);

        Graph graph = GraphReader.readGraphFromJson(filename);
        int source = GraphReader.getSourceFromJson(filename);

        runAnalysis(graph, filename, source);
    }

    private static void runAnalysis(Graph graph, String graphName, int source) {
        System.out.println("\n=== Graph Analysis: " + graphName + " ===");
        System.out.println("Total vertices: " + graph.getVertices());
        System.out.println("Source node: " + source);

        printGraphStructure(graph);

        System.out.println("\n--- Strongly Connected Components (SCC) ---");
        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult sccResult = tarjan.findSCCs();
        sccResult.printResults();

        System.out.println("\n--- Condensation Graph & Topological Sort ---");
        CondensationGraph condensation = new CondensationGraph(graph, sccResult);
        List<Integer> topoOrder = condensation.getTopologicalOrder();
        System.out.println("Topological Order of Components: " + topoOrder);

        System.out.println("\nNode → Component Mapping:");
        condensation.getNodeToComponent().forEach((node, component) ->
                System.out.println("Node " + node + " → Component " + component));

        System.out.println("\n--- Shortest Path Analysis ---");
        performShortestPathAnalysis(graph, condensation, source);

        System.out.println("\n--- Critical Path Analysis ---");
        performCriticalPathAnalysis(condensation);
    }

    private static void printGraphStructure(Graph graph) {
        System.out.println("\nGraph Structure:");
        for (int i = 0; i < graph.getVertices(); i++) {
            System.out.print("Node " + i + " -> ");
            for (Edge edge : graph.getEdges(i)) {
                System.out.print(edge.getTo() + "(" + edge.getWeight() + ") ");
            }
            System.out.println();
        }
    }

    private static void performShortestPathAnalysis(Graph graph, CondensationGraph condensation, int source) {
        try {
            DAGShortestPath dagSP = new DAGShortestPath(graph);
            ShortestPathResult spResult = dagSP.findShortestPaths(source);
            spResult.printResults();

            System.out.println("\nShortest Paths from source " + source + ":");
            for (int i = 0; i < graph.getVertices(); i++) {
                List<Integer> path = spResult.reconstructPath(i);
                if (!path.isEmpty()) {
                    System.out.printf("To node %d: %s (distance: %.2f)%n",
                            i, path, spResult.getDistances()[i]);
                } else if (i != source) {
                    System.out.println("To node " + i + ": No path");
                }
            }
        } catch (Exception e) {
            System.out.println("Detected cycles — using condensation graph for shortest paths");

            Graph condGraph = condensation.getCondensationGraph();
            DAGShortestPath condSP = new DAGShortestPath(condGraph);
            ShortestPathResult condResult = condSP.findShortestPaths(
                    condensation.getNodeToComponent().get(source));
            condResult.printResults();
        }
    }

    private static void performCriticalPathAnalysis(CondensationGraph condensation) {
        Graph condGraph = condensation.getCondensationGraph();
        DAGShortestPath dagSP = new DAGShortestPath(condGraph);
        CriticalPathResult cpResult = dagSP.findCriticalPath();

        cpResult.printResults();
        System.out.println("\nCritical Path Length: " + cpResult.getCriticalPathLength());
        System.out.println("Critical Path (component order): " + cpResult.getCriticalPath());
    }
}
