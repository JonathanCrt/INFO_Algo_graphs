package fr.umlv.info2.graphs.test;

import fr.umlv.info2.graphs.AdjGraph;
import fr.umlv.info2.graphs.Graphs;
import fr.umlv.info2.graphs.MatGraph;
import org.junit.jupiter.api.Test;

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

        assertEquals(List.of(0, 1, 3, 2), Graphs.DFS(adjGraph, 0));
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



}