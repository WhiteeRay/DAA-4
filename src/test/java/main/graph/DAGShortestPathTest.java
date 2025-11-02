package graph;

import graph.dagsp.DAGShortestPath;
import graph.dagsp.*;
import graph.models.Graph;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;


class DAGShortestPathTest {

    @Test
    void testShortestPath() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 2);

        DAGShortestPath sp = new DAGShortestPath(graph);
        ShortestPathResult result = sp.findShortestPaths(0);

        assertEquals(0, result.getDistances()[0]);
        assertEquals(2, result.getDistances()[1]);
        assertEquals(1, result.getDistances()[2]);
        assertEquals(3, result.getDistances()[3]); // 0->2->3 = 1+2=3
    }
}
