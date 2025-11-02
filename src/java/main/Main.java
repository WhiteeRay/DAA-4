

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
        if (true) {

            try {
                processJsonFile("data/tasks.json");
            } catch (IOException e) {
                System.err.println("Error processing file: " + e.getMessage());
            }
        } else {
            System.out.println("No input file provided.");

        }
    }

    public static void processJsonFile(String filename) throws IOException {
        System.out.println("=== Processing: " + filename + " ===");

        Graph graph = GraphReader.readGraphFromJson(filename);
        int source = GraphReader.getSourceFromJson(filename);

        runCompleteAnalysis(graph, filename, source);
    }

    public static void runCompleteAnalysis(Graph graph, String graphName, int source) {
        System.out.println("=== Analyzing: " + graphName + " ===");
        System.out.println("Vertices: " + graph.getVertices());
        System.out.println("Source node: " + source);

        System.out.println("\nGraph Structure:");
        for (int i = 0; i < graph.getVertices(); i++) {
            System.out.print("Node " + i + " -> ");
            for (Edge edge : graph.getEdges(i)) {
                System.out.print(edge.getTo() + "(" + edge.getWeight() + ") ");
            }
            System.out.println();
        }


        System.out.println("\n--- SCC Analysis ---");
        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult sccResult = tarjan.findSCCs();
        sccResult.printResults();


        System.out.println("\n--- Condensation Graph & Topological Sort ---");
        CondensationGraph condensation = new CondensationGraph(graph, sccResult);
        List<Integer> topoOrder = condensation.getTopologicalOrder();
        System.out.println("Topological Order of Components: " + topoOrder);


        System.out.println("Node to Component Mapping:");
        for (int i = 0; i < graph.getVertices(); i++) {
            System.out.println("Node " + i + " -> Component " +
                    condensation.getNodeToComponent().get(i));
        }


        System.out.println("\n--- Shortest Paths Analysis ---");


        try {
            DAGShortestPath originalDAGSP = new DAGShortestPath(graph);
            ShortestPathResult spResult = originalDAGSP.findShortestPaths(source);
            spResult.printResults();


            System.out.println("\nShortest Paths from source " + source + ":");
            for (int i = 0; i < graph.getVertices(); i++) {
                List<Integer> path = spResult.reconstructPath(i);
                if (!path.isEmpty()) {
                    System.out.println("To node " + i + ": " + path +
                            " (distance: " + spResult.getDistances()[i] + ")");
                } else if (i != source) {
                    System.out.println("To node " + i + ": No path");
                }
            }
        } catch (Exception e) {
            System.out.println("Original graph has cycles, using condensation graph for shortest paths");


            Graph condGraph = condensation.getCondensationGraph();
            DAGShortestPath condDAGSP = new DAGShortestPath(condGraph);
            ShortestPathResult condSPResult = condDAGSP.findShortestPaths(
                    condensation.getNodeToComponent().get(source));
            condSPResult.printResults();
        }


        System.out.println("\n--- Critical Path Analysis ---");
        Graph condGraph = condensation.getCondensationGraph();
        DAGShortestPath criticalPathFinder = new DAGShortestPath(condGraph);
        CriticalPathResult criticalPath = criticalPathFinder.findCriticalPath();
        criticalPath.printResults();


        List<Integer> criticalPathNodes = criticalPath.getCriticalPath();
        System.out.println("Critical Path Length: " + criticalPath.getCriticalPathLength());
        System.out.println("Critical Path (component order): " + criticalPathNodes);
    }


}