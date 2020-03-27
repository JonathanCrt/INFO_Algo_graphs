package fr.umlv.info2.graphs.test;

import fr.umlv.info2.graphs.AdjGraph;
import fr.umlv.info2.graphs.Graph;
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

        assertEquals(List.of(0, 1, 2, 3), Graphs.BFS(adjGraph , 0));
    }


    @Test
    void testTimedDFS() {
        var matGraph = new MatGraph(4);

        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(1, 2, 3);
        matGraph.addEdge(1, 3, 6);
        matGraph.addEdge(2, 0, 1);
        matGraph.addEdge(3, 1, 2);

        assertEquals("[[1, 6], [2, 5], [3, 4], [7, 8]]" , Arrays.deepToString(Graphs.timedDepthFirstSearch(matGraph, 0)));
    }





}