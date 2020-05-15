package fr.umlv.info2.graphs.test;

import fr.umlv.info2.graphs.AdjGraph;
import fr.umlv.info2.graphs.Graphs;
import fr.umlv.info2.graphs.MatGraph;
import fr.umlv.info2.graphs.ShortestPathFromOneVertex;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphsPathTest {


    @Test
    public void testDFS() {
        AdjGraph adjGraph = new AdjGraph(4);

        adjGraph.addEdge(0, 1, 1);
        adjGraph.addEdge(1, 2, 3);
        adjGraph.addEdge(1, 3, 6);
        adjGraph.addEdge(2, 0, 1);
        adjGraph.addEdge(3, 1, 2);

        assertEquals(List.of(0, 1, 2, 3), Graphs.DFS(adjGraph, 0));
    }

    @Test
    public void testDFS2() {
        var matGraph = new MatGraph(5);
        matGraph.addEdge(0, 2, 4);
        matGraph.addEdge(0, 3, 2);
        matGraph.addEdge(2, 0, 7);
        matGraph.addEdge(2, 1, 8);
        matGraph.addEdge(2, 2, 5);
        matGraph.addEdge(4, 0, 3);
        matGraph.addEdge(0, 4, 3);
        matGraph.addEdge(1, 1, 7);
        matGraph.addEdge(4, 2, 2);
        matGraph.addEdge(4, 3, 6);
        assertEquals(List.of(1, 2, 0, 3, 4), Graphs.DFS(matGraph, 1));
    }


    @Test
    public void testBFS() {
        AdjGraph adjGraph = new AdjGraph(4);
        adjGraph.addEdge(0, 1, 1);
        adjGraph.addEdge(1, 2, 3);
        adjGraph.addEdge(1, 3, 6);
        adjGraph.addEdge(2, 0, 1);
        adjGraph.addEdge(3, 1, 2);

        assertEquals(List.of(0, 1, 2, 3), Graphs.BFS(adjGraph, 0));
    }


    @Test
    public void testBFS2() {
        var matGraph = new MatGraph(5);
        matGraph.addEdge(0, 3, 1);
        matGraph.addEdge(2, 3, 5);
        matGraph.addEdge(3, 0, 10);
        matGraph.addEdge(0, 4, 2);
        matGraph.addEdge(1, 1, 6);
        matGraph.addEdge(2, 4, 8);
        matGraph.addEdge(1, 4, 8);
        matGraph.addEdge(2, 0, 22);
        matGraph.addEdge(2, 2, 17);
        matGraph.addEdge(4, 4, 5);
        assertEquals(List.of(3, 0, 4), Graphs.BFS(matGraph, 3));
    }


    @Test
    public void testTimedDFS() {

        var matGraph = new MatGraph(9);
        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(0, 2, 1);
        matGraph.addEdge(2, 1, 1);
        matGraph.addEdge(2, 4, 1);
        matGraph.addEdge(3, 2, 1);
        matGraph.addEdge(8, 3, 1);
        matGraph.addEdge(3, 7, 1);
        matGraph.addEdge(7, 8, 1);
        matGraph.addEdge(4, 6, 1);
        matGraph.addEdge(6, 5, 1);
        matGraph.addEdge(5, 4, 1);
        matGraph.addEdge(5, 0, 1);

        matGraph.toGraphviz();
        assertEquals("[[0, 11], [1, 2], [3, 10], [12, 17], [4, 9], [6, 7], [5, 8], [13, 16], [14, 15]]", Arrays.deepToString(Graphs.timedDepthFirstSearch(matGraph, 0)));
    }

    @Test
    public void testTopologicalSort() {
        var matGraph = new MatGraph(5);
        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(0, 4, 1);
        matGraph.addEdge(1, 2, 1);
        matGraph.addEdge(1, 3, 1);

        var adjGraph = new AdjGraph(5);
        adjGraph.addEdge(0, 1, 1);
        adjGraph.addEdge(0, 4, 1);
        adjGraph.addEdge(1, 2, 1);
        adjGraph.addEdge(1, 3, 1);

        var adjGraph2 = new AdjGraph(4);
        adjGraph2.addEdge(0, 1, 1);
        adjGraph2.addEdge(1, 2, 3);
        adjGraph2.addEdge(1, 3, 6);
        adjGraph2.addEdge(2, 0, 1);
        adjGraph2.addEdge(3, 1, 2);


        assertEquals(List.of(0, 4, 1, 3, 2), Graphs.topologicalSort(matGraph, false));
        assertEquals(List.of(0, 4, 1, 3, 2), Graphs.topologicalSort(adjGraph, true));
        assertThrows(IllegalStateException.class, () -> Graphs.topologicalSort(adjGraph2, true));
    }

    @Test
    public void testTopologicalSort2() {
        var matGraph = new MatGraph(5);
        matGraph.addEdge(2, 0, 4);
        matGraph.addEdge(4, 1, 5);
        matGraph.addEdge(4, 3, 9);
        matGraph.addEdge(2, 1, 12);
        matGraph.addEdge(2, 3, 17);
        matGraph.addEdge(3, 3, 4);
        matGraph.addEdge(0, 1, 7);
        matGraph.addEdge(1, 4, 2);
        matGraph.addEdge(3, 4, 2);
        matGraph.addEdge(4, 4, 7);
        assertEquals(List.of(2, 0, 1, 4, 3), Graphs.topologicalSort(matGraph, false));
    }


    @Test
    public void testSCC() {
        var kosaraju = new MatGraph(9);
        kosaraju.addEdge(0, 1, 1);
        kosaraju.addEdge(0, 2, 2);
        kosaraju.addEdge(2, 1, 3);
        kosaraju.addEdge(2, 4, 6);
        kosaraju.addEdge(3, 2, 5);
        kosaraju.addEdge(3, 7, 10);
        kosaraju.addEdge(4, 6, 10);
        kosaraju.addEdge(5, 0, 5);
        kosaraju.addEdge(5, 4, 9);
        kosaraju.addEdge(6, 5, 11);
        kosaraju.addEdge(7, 8, 15);
        kosaraju.addEdge(8, 3, 11);

        // [3, 7, 8];  [0, 2, 4, 5, 6] ; [1]
        assertEquals(List.of(List.of(3, 8, 7), List.of(0, 5, 6, 4, 2), List.of(1)), Graphs.scc(kosaraju));

    }

    @Test
    public void testSCC2() {
        var kosaraju = new MatGraph(5);
        kosaraju.addEdge(0, 0, 1);
        kosaraju.addEdge(1, 0, 3);
        kosaraju.addEdge(2, 3, 4);
        kosaraju.addEdge(2, 4, 3);
        kosaraju.addEdge(1, 3, 5);
        kosaraju.addEdge(2, 0, 3);
        kosaraju.addEdge(2, 2, 4);
        kosaraju.addEdge(3, 0, 4);
        kosaraju.addEdge(3, 2, 5);
        kosaraju.addEdge(4, 4, 7);
        assertEquals(List.of(List.of(1), List.of(3, 2), List.of(4), List.of(0)), Graphs.scc(kosaraju));
    }


    @Test
    public void testBellmanFord() {

        var matGraph = new MatGraph(5);
        matGraph.addEdge(0, 1, 2);
        matGraph.addEdge(0, 2, 5);
        matGraph.addEdge(1, 2, 1);
        matGraph.addEdge(1, 4, 4);
        matGraph.addEdge(2, 3, 4);
        matGraph.addEdge(2, 4, 2);
        matGraph.addEdge(3, 2, 2);

        assertEquals("0 [0, 2, 3, 7, 5] [0, 0, 1, 2, 2]", Graphs.bellmanFord(matGraph, 0).toString());
    }

    // dont't forget to test negative circle


    @Test
    public void testDijkstra() {

        var matGraph = new MatGraph(5);
        matGraph.addEdge(0, 1, 8); // A -> B : 8
        matGraph.addEdge(0, 2, 2); // A -> C : 2
        matGraph.addEdge(1, 3, 2); // B -> D : 2
        matGraph.addEdge(2, 1, 5); // C -> B : 5
        matGraph.addEdge(2, 4, 2); // C -> E : 2
        matGraph.addEdge(4, 1, 3); // E -> B : 3
        matGraph.addEdge(4, 3, 8); // E -> D : 8

        assertEquals("0 [0, 7, 2, 10, 4] [null, 2, 0, 1, 2]", Graphs.dijkstra(matGraph, 0).toString());
    }


    @Test
    public void testFloydWarshall() {
        var matGraph = new MatGraph(4);
        matGraph.addEdge(0, 1, 8);
        matGraph.addEdge(1, 2, 1);
        matGraph.addEdge(2, 0, 4);
        matGraph.addEdge(2, 1, 5);
        matGraph.addEdge(3, 1, 2);
        matGraph.addEdge(3, 2, 9);

        assertEquals("[0, 3, 4, 1] [5, 0, 1, 6] [4, 7, 0, 5] [7, 2, 3, 0]"+
                " [0, 3, 3, 0] [2, 1, 1, 2] [2, 3, 2, 0] [2, 3, 2, 3]",
                Graphs.floydWarshall(matGraph).toString());
    }

}