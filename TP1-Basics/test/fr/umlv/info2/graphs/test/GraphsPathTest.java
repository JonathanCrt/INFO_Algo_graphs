package fr.umlv.info2.graphs.test;

import fr.umlv.info2.graphs.AdjGraph;
import fr.umlv.info2.graphs.Graphs;
import fr.umlv.info2.graphs.MatGraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphsPathTest {


    @Test
    void testDFS() {
        AdjGraph adjGraph = new AdjGraph(4);

        adjGraph.addEdge(0, 1, 1);
        adjGraph.addEdge(1, 2, 3);
        adjGraph.addEdge(1, 3, 6);
        adjGraph.addEdge(2, 0, 1);
        adjGraph.addEdge(3, 1, 2);

        assertEquals(List.of(0, 1, 2, 3), Graphs.DFS(adjGraph, 0));
    }


    @Test
    void testBFS() {
        AdjGraph adjGraph = new AdjGraph(4);
        adjGraph.addEdge(0, 1, 1);
        adjGraph.addEdge(1, 2, 3);
        adjGraph.addEdge(1, 3, 6);
        adjGraph.addEdge(2, 0, 1);
        adjGraph.addEdge(3, 1, 2);

        assertEquals(List.of(0, 1, 2, 3), Graphs.BFS(adjGraph, 0));
    }


    @Test
    void testTimedDFS() {

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
    void testTopologicalSort() {
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


        assertEquals(List.of(0, 1, 2, 3, 4), Graphs.topologicalSort(matGraph, false));
        assertEquals(List.of(0, 1, 2, 3, 4), Graphs.topologicalSort(adjGraph, true));
        assertEquals(List.of(0, 1, 2, 3), Graphs.topologicalSort(adjGraph2, true));
    }

    @Test
    void testSCC() {
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

        // [0, 2, 4, 5, 6]  ; [3, 7, 8]; [1]
        assertEquals(List.of(List.of(0, 1, 2, 4, 6, 5), List.of(3, 7, 8)), Graphs.scc(kosaraju));

    }

    @Test
    void testBellmanFord() {

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

}