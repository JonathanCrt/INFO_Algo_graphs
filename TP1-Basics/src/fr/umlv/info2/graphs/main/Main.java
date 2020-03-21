package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.AdjGraph;
import fr.umlv.info2.graphs.Graph;
import fr.umlv.info2.graphs.Graphs;
import fr.umlv.info2.graphs.MatGraph;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        var numberOfVertices = 4;
        var matGraph = new MatGraph(numberOfVertices);

        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(1, 2, 3);
        matGraph.addEdge(1, 3, 6);
        matGraph.addEdge(2, 0, 1);
        matGraph.addEdge(3, 1, 2);
        
        System.out.println(matGraph.toGraphviz());

        var adjGraph = new AdjGraph(numberOfVertices);

        adjGraph.addEdge(0, 1, 1);
        adjGraph.addEdge(1, 2, 3);
        adjGraph.addEdge(1, 3, 6);
        adjGraph.addEdge(2, 0, 1);
        adjGraph.addEdge(3, 1, 2);

        System.out.println(adjGraph.toGraphviz());
        System.out.println("Weight : " + adjGraph.getWeight(3, 1));

        System.out.println(Graphs.DFS(matGraph, 1));
        System.out.println(Graphs.DFS(adjGraph, 0));
        System.out.println(Graphs.BFS(adjGraph, 0));

        System.out.println(Graphs.createRandomGraph(4, 4, 19));
    }
}
