package fr.umlv.info2.graphs;

import java.util.Iterator;
import java.util.function.Consumer;

public interface Graph {
    int numberOfEdges();

    int numberOfVertices();

    void addEdges(int i, int j, int value);

    boolean isEdge(int i, int j);

    int getWeight(int i, int j);

    Iterator<Edge> edgeIterator(int i);

    void forEach(int i, Consumer<Edge> consumer);

    String toGraphviz();
}
