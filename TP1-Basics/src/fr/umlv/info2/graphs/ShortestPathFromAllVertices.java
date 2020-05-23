package fr.umlv.info2.graphs;

import java.util.Arrays;
import java.util.Stack;
import java.util.StringJoiner;

public class ShortestPathFromAllVertices {
    private final int[][] d;
    private final int[][] pi;

    public ShortestPathFromAllVertices(int[][] d, int[][] pi) {
        this.d = d;
        this.pi = pi;
    }

    @Override
    public String toString() {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < d.length; i++) {
            bf.append(Arrays.toString(d[i])).append("\t").append(Arrays.toString(pi[i])).append("\n");
        }

        return bf.toString();
    }

    public void printShortestPath(int source, int destination) {
        if(source == destination) {
            System.out.println("Source vertex  is equal to destination vertex");
            return;
        }
        var shortestPathValues = new Stack<Integer>();
        var joiner = new StringJoiner("=> ");
        var target = destination;

        for (; destination != source; ) {
            shortestPathValues.push(destination);
            destination = pi[source][destination];
        }
        shortestPathValues.push(destination);

        while (!shortestPathValues.isEmpty()) {
            joiner.add(shortestPathValues.pop() + " ");
        }

        System.out.println(joiner.toString());
    }
}