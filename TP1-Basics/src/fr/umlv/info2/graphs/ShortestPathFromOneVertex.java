package fr.umlv.info2.graphs;

import java.util.ArrayList;
import java.util.Arrays;

public class ShortestPathFromOneVertex {
    private final int source;
    private final int[] d;
    private final Integer[] pi;

    public ShortestPathFromOneVertex(int vertex, int[] d, Integer[] pi) {
        this.source = vertex;
        this.d = d;
        this.pi = pi;
    }

    @Override
    public String toString() {
        return source + " " + Arrays.toString(d) + " " + Arrays.toString(pi);
    }

    private void printShortestPathTo_Rec(int destination, StringBuilder stringBuilder) {
        if(this.source == destination) {
            System.out.println("Source vertex  is equal to destination vertex");
            return;
        }
        this.printShortestPathTo_Rec(this.pi[destination], stringBuilder);
        stringBuilder.append(" ==> ")
                .append(destination);
    }

    public void printShortestPathTo(int destination) {
        var stringBuilder = new StringBuilder("New shortest path from vertex ");
        stringBuilder.append(this.source)
                .append(" to vertex ")
                .append(destination)
                .append(":  ")
                .append(source);
        this.printShortestPathTo_Rec(destination, stringBuilder);
        System.out.println(stringBuilder);
    }






}