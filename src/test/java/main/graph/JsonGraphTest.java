package graph;

import graph.models.Graph;
import graph.scc.SCCResult;
import graph.scc.TarjanSCC;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

class JsonGraphTest {

    @Test
    void testProvidedJsonStructure() throws IOException {
        // This test would use your provided tasks.json
        Graph graph = GraphReader.readGraphFromJson("data/tasks.json");
        int source = GraphReader.getSourceFromJson("data/tasks.json");

        assertEquals(8, graph.getVertices());
        assertEquals(4, source);

        // Verify edges
        assertEquals(1, graph.getEdges(0).size());
        assertEquals(1, graph.getEdges(1).size());
        assertEquals(1, graph.getEdges(2).size());
        assertEquals(1, graph.getEdges(3).size());
        assertEquals(1, graph.getEdges(4).size());
        assertEquals(1, graph.getEdges(5).size());
        assertEquals(1, graph.getEdges(6).size());
        assertEquals(0, graph.getEdges(7).size());

        // Test SCC detection
        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();
        List<List<Integer>> components = result.getComponents();

        // Should find the cycle 1-2-3 as one component
        boolean foundCycleComponent = false;
        for (List<Integer> component : components) {
            if (component.contains(1) && component.contains(2) && component.contains(3)) {
                assertEquals(3, component.size());
                foundCycleComponent = true;
            }
        }
        assertTrue(foundCycleComponent, "Should find cycle component 1-2-3");
    }
}