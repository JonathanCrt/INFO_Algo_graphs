package fr.umlv.info2.graphs.test;

import fr.umlv.info2.graphs.AdjGraph;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AdjGraphTest {
    @Test
    public void testCreateAdjGraph () {
        var graph = new AdjGraph(50);
    }

    @Test
    public void testCreateMatgraphWithZeroVertexCount() {
        assertThrows(IllegalArgumentException.class, () -> new AdjGraph(0));
    }

    @Test
    public void testnumberOfEdges() {
        var numberOfVertices = 17;
        var AdjGraph = new AdjGraph(numberOfVertices);

        AdjGraph.addEdge(0, 1, 1);
        AdjGraph.addEdge(1, 2, 3);
        AdjGraph.addEdge(1, 3, 6);
        AdjGraph.addEdge(2, 8, 1);
        AdjGraph.addEdge(3, 8, 2);

        assertEquals(5, AdjGraph.numberOfEdges());
    }

    @Test
    public void numberOfVertices() {
        var AdjGraph = new AdjGraph(5);
        assertEquals(5, AdjGraph.numberOfVertices());

    }

    @Test
    public void testAddOneEdge() {
        var AdjGraph = new AdjGraph(5);
        AdjGraph.addEdge(0, 1, 6);
        assertEquals(6, AdjGraph.getWeight(0, 1));
    }

    @Test
    public void testAddEdgeInvalidWeight() {
        var AdjGraph = new AdjGraph(7);
        assertThrows(IllegalArgumentException.class, () -> AdjGraph.addEdge(3, 4, -2));
    }

    @Test
    public void testAddEdgeZeroWeight() {
        var graph = new AdjGraph(7);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(3, 4, 0));
    }

    @Test
    public void testHasEdgeValid() {
        var graph = new AdjGraph(5);
        assertAll(
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.getWeight(-1, 3)),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.getWeight(2, -1)),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.getWeight(5, 2)),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.getWeight(3, 5))
        );
    }

    @Test
    public void testGetWeightEmpty() {
        var numberOfVertices = 20;
        var AdjGraph = new AdjGraph(20);
        for (var i = 0; i < numberOfVertices; i++) {
            for (var j = 0; j < numberOfVertices; j++) {
                assertTrue(AdjGraph.getWeight(i, j) == 0);
            }
        }
    }

    @Test
    public void testEdgesNullConsumer() {
        assertThrows(NullPointerException.class, () -> new AdjGraph(17).forEachEdge(0, null));
    }


    @Test
    public void testEdgeIterator() {
        var graph = new AdjGraph(6);

        graph.addEdge(1, 2, 222);
        graph.addEdge(1, 5, 555);

        var iterator = graph.edgeIterator(1);
        assertEquals("1 -- 2 ( 222 )", iterator.next().toString());
        assertEquals("1 -- 5 ( 555 )", iterator.next().toString());
    }

    @Test
    public void testNeighborsEmptyHasNext() {
        var graph = new AdjGraph(6);
        var iterator = graph.edgeIterator(0);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNeighborsOutOfRange() {
        var graph = new AdjGraph(6);
        assertAll(
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.edgeIterator(10)),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.edgeIterator(-2))
        );
    }

    @Test
    public void testNeighborsEmptyNext() {
        var graph = new AdjGraph(6);
        assertThrows(NoSuchElementException.class, () -> graph.edgeIterator(0).next());
    }
}