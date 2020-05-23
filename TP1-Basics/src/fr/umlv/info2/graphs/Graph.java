package fr.umlv.info2.graphs;

import java.util.Iterator;
import java.util.function.Consumer;

public interface Graph {

    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges on the graph
     */
    int numberOfEdges();

    /**
     * Returns the number of vertices in the graph
     *
     * @return the number of vertices on the graph
     */
    int numberOfVertices();

    /**
     * Adds an edge oriented to the graph
     *
     * @param i     the 1st end of the edge
     * @param j     the 2nds end of the edge
     * @param value the weight of edge
     */
    void addEdge(int i, int j, int value);

    /**
     * Tests the existence of a given edge
     *
     * @param i the 1st end of the edge
     * @param j the 2nds end of the edge
     * @return true if there is an edge between i and j false otherwise
     */
    boolean isEdge(int i, int j);

    /**
     * Returns the weight of a given edge
     *
     * @param i the 1st end of the edge
     * @param j the 2nds end of the edge
     * @return the weight of the edge between i and j
     */
    int getWeight(int i, int j);

    /**
     * Returns an iterator on all neighbors of a given vertex.
     *
     * @param i the vertex from which the edges provided by the iterator start
     * @return an iterator on all of the vertex's neighbors i
     */
    Iterator<Edge> edgeIterator(int i);

    /**
     * Performs an action on all edges of a given vertex.
     *
     * @param i        the vertex from which the processed edges start
     * @param consumer the action performed on all edges adjacent to i
     */
    void forEachEdge(int i, Consumer<Edge> consumer);

    /**
     * display the graph
     *
     * @return graph display (string)
     */
    default String toGraphviz() {
        var sb = new StringBuilder();
        sb.append("digraph  {").append(System.lineSeparator());

        for (var i = 0; i < this.numberOfVertices(); i++) {
            sb.append(i + ";").append(System.lineSeparator());

            this.edgeIterator(i).forEachRemaining(
                    edge -> {
                        var label = "[ label=\"" + edge.getValue() + "\" ]";
                        sb.append(edge.getStart() + " -> " + edge.getEnd() + " " + label + " ;")
                                .append(System.lineSeparator());
                    }
            );
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Reverse the edges of the graph
     *
     * @return transposed graph
     */
    default Graph transpose() {
        var graph = this;
        var numberOfVertices = graph.numberOfVertices();
        var matGraph = new MatGraph(numberOfVertices);

        for (var vertex = 0; vertex < numberOfVertices; vertex++) {
            graph.forEachEdge(vertex, edge -> matGraph.addEdge(edge.getEnd(), edge.getStart(), edge.getValue()));
        }
        return matGraph;
    }
}
