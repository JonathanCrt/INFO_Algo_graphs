package fr.umlv.info2.graphs;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class MatGraph implements Graph {
    private final int[][] mat;
    private final int n; //number of vertices


    public MatGraph(int[][] mat, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.mat = mat;
        this.n = n;
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
        return n;
    }

    @Override
    public void addEdges(int i, int j, int value) {
        Objects.requireNonNull(value);
        this.mat[i][j] = value;
    }

    @Override
    public boolean isEdge(int i, int j) {

        if (i > n || i < 0 || j > n || j < 0) {
            throw new IllegalArgumentException();
        }
        return mat[i][j] != 0;

    }

    @Override
    public int getWeight(int i, int j) {
        if (i > n || i < 0 || j > n || j < 0) {
            throw new IllegalArgumentException();
        }
        return mat[i][j];
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) { // Return an iterator of edge


        return new Iterator<>() {

            private int j = 0;

            @Override
            public boolean hasNext() {
                return j < n;
            }

            @Override
            public Edge next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                Edge edge = new Edge(i, j, mat[i][j]);
                j++;
                return edge;
            }

        };
    }

    @Override
    public void forEach(int i, Consumer<Edge> consumer) {
        consumer.accept(this.edgeIterator(i).next());
    }

    @Override
    public String toGraphviz() {

    }
}
