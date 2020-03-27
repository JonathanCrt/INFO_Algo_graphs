package fr.umlv.info2.graphs.test;

import fr.umlv.info2.graphs.MatGraph;
import org.junit.jupiter.api.Test;


import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("static-method")
public class MatGraphTest {

    @Test
    public void testCreateMatGraph () {
        var graph = new MatGraph(50);
    }

    @Test
    public void testCreateMatgraphWithZeroVertexCount() {
        assertThrows(IllegalArgumentException.class, () -> new MatGraph(0));
    }

    @Test
    public void testnumberOfEdges() {
        var numberOfVertices = 17;
        var matGraph = new MatGraph(numberOfVertices);

        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(1, 2, 3);
        matGraph.addEdge(1, 3, 6);
        matGraph.addEdge(2, 8, 1);
        matGraph.addEdge(3, 8, 2);

        assertEquals(5, matGraph.numberOfEdges());
    }

    @Test
    public void numberOfVertices() {
        var matGraph = new MatGraph(5);
        assertEquals(5, matGraph.numberOfVertices());

    }

    @Test
    public void testAddOneEdge() {
        var matGraph = new MatGraph(5);
        matGraph.addEdge(0, 1, 6);
        assertEquals(6, matGraph.getWeight(0, 1));
    }

    @Test
    public void testAddEdgeInvalidWeight() {
        var matGraph = new MatGraph(7);
        assertThrows(IllegalArgumentException.class, () -> matGraph.addEdge(3, 4, -2));
    }

    @Test
    public void testAddEdgeZeroWeight() {
        var graph = new MatGraph(7);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(3, 4, 0));
    }

    @Test
    public void testAddEdgeTwice() {
        var graph = new MatGraph(7);
        graph.addEdge(3, 4, 5);
        graph.addEdge(3, 4, 19);
        assertEquals(19, graph.getWeight(3, 4));
    }


    @Test
    public void testHasEdgeValid() {
        var graph = new MatGraph(5);
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
        var matGraph = new MatGraph(20);
        for (var i = 0; i < numberOfVertices; i++) {
            for (var j = 0; j < numberOfVertices; j++) {
                assertTrue(matGraph.getWeight(i, j) == 0);
            }
        }
    }

    @Test
    public void testEdgesNullConsumer() {
        assertThrows(NullPointerException.class, () -> new MatGraph(17).forEachEdge(0, null));
    }


    @Test
    public void testEdgeIterator() {
        var graph = new MatGraph(6);

        graph.addEdge(1, 2, 222);
        graph.addEdge(1, 5, 555);

        var iterator = graph.edgeIterator(1);
        assertEquals("1 -- 2 ( 222 )", iterator.next().toString());
        assertEquals("1 -- 5 ( 555 )", iterator.next().toString());
    }

    @Test
    public void testNeighborsEmptyHasNext() {
        var graph = new MatGraph(6);
        var iterator = graph.edgeIterator(0);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNeighborsOutOfRange() {
        var graph = new MatGraph(6);
        assertAll(
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.edgeIterator(10)),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> graph.edgeIterator(-2))
        );
    }

    @Test
    public void testNeighborsEmptyNext() {
        var graph = new MatGraph(6);
        assertThrows(NoSuchElementException.class, () -> graph.edgeIterator(0).next());
    }

}