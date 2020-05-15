package fr.umlv.info2.graphs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class MatGraph implements Graph {
    private final int[][] mat;
    private final int numberOfVertices; //number of vertices


    public MatGraph(int numberOfVertices) {
        if (numberOfVertices == 0) {
            throw new IllegalArgumentException();
        }
        this.mat = new int[numberOfVertices][numberOfVertices];
        this.numberOfVertices = numberOfVertices;
    }


    private void checkEdge(int i, int j) {
        Objects.checkIndex(i, numberOfVertices);
        Objects.checkIndex(j, numberOfVertices);
    }


    @Override
    public int numberOfEdges() {
        var count = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int numberOfVertices() {
        return this.numberOfVertices;
    }

    @Override
    public void addEdge(int i, int j, int weight) {
        this.checkEdge(i, j);
        Objects.requireNonNull(weight);
//        if (weight <= 0) {
//            throw new IllegalArgumentException();
//        }
        this.mat[i][j] = weight;
    }

    @Override
    public boolean isEdge(int i, int j) {
        if (i > numberOfVertices || i < 0 || j > numberOfVertices || j < 0) {
            throw new IllegalArgumentException();
        }
        return this.mat[i][j] != 0;
    }

    @Override
    public int getWeight(int i, int j) {
        this.checkEdge(i, j);
        return this.mat[i][j];
    }



    @Override
    public Iterator<Edge> edgeIterator(int i) { // Return an iterator of edge

        var neighbours = new ArrayList<Edge>();
        for (var j = 0 ; j < mat.length ; j++) {
            if (mat[i][j] != 0) {
                neighbours.add(new Edge(i, j, mat[i][j]));
            }
        }
        return neighbours.iterator();
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        Objects.requireNonNull(consumer);
        if (i >= numberOfVertices) {
            throw new IndexOutOfBoundsException();
        }
        edgeIterator(i).forEachRemaining(consumer);
    }
}
