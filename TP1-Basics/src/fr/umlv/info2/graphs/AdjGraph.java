package fr.umlv.info2.graphs;

import java.util.*;
import java.util.function.Consumer;

public class AdjGraph implements Graph {
    private ArrayList<LinkedList<Edge>> adj;
    private final int numberOfVertices; // number of vertices
    private int numberOfEdges;

    public AdjGraph(int n) {
        if (n == 0) {
            throw new IllegalArgumentException();
        }
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(i, new LinkedList<>());
        }
        this.numberOfVertices = n;
        this.numberOfEdges = 0;
    }


    private void checkEdge(int i, int j) {
        Objects.checkIndex(i, numberOfVertices);
        Objects.checkIndex(j, numberOfVertices);
    }


    @Override
    public int numberOfEdges() {
        return this.numberOfEdges;
    }

    @Override
    public int numberOfVertices() {
        return this.numberOfVertices;
    }

    @Override
    public void addEdge(int i, int j, int weight) {
        this.checkEdge(i, j);
        if (weight <= 0) {
            throw new IllegalArgumentException();
        }
        var edge = new Edge(i, j, weight);

        if (!adj.get(i).contains(edge)) {
            adj.get(i).add(new Edge(i, j, weight));
        }
        this.numberOfEdges++;
    }

    @Override
    public boolean isEdge(int i, int j) {
        this.checkEdge(i, j);
        LinkedList<Edge> edges = adj.get(i);
        for (var edge : edges) {
            if (edge.getEnd() == j) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getWeight(int i, int j) {
        this.checkEdge(i, j);
        for (var edge : adj.get(i)) {
            if (edge.getEnd() == j) {
                return edge.getValue();
            }
        }
        return 0;
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) {
        return adj.stream().flatMap(Collection::stream).filter(edge -> isEdge(i, edge.getEnd())).iterator();
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        edgeIterator(i).forEachRemaining(consumer);
    }

    @Override
    public String toGraphviz() {
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
}
