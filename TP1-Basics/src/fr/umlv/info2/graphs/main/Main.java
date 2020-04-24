package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.AdjGraph;
import fr.umlv.info2.graphs.Graphs;
import fr.umlv.info2.graphs.MatGraph;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) {
        /*
        var numberOfVertices = 4;
        var matGraph = new MatGraph(numberOfVertices);

        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(1, 2, 3);
        matGraph.addEdge(1, 3, 6);
        matGraph.addEdge(2, 0, 1);
        matGraph.addEdge(3, 1, 2);

        System.out.println(matGraph.toGraphviz());
        */
        /*
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
        */
        //System.out.println(Graphs.timedDepthFirstSearch(matGraph, 0));

        /*
        try {
            var path = Paths.get("C:/Users/jonat/Documents/GitHub/INFO_Algo_graphs/TP1-Basics/src/fr/umlv/info2/graphs/graphEx.txt");
            var matFile = Graphs.createGraphFromMatrixFile(path);
            LOGGER.info("Graph from file : " + matFile);
        } catch (IOException e) {
            LOGGER.info("some IO errors occured");
        }
        */

        var matGraph = new MatGraph(9);
        matGraph.addEdge(0, 1, 1);
        matGraph.addEdge(0, 2, 1);
        matGraph.addEdge(2, 1, 1);
        matGraph.addEdge(2, 4, 1);
        matGraph.addEdge(3, 2, 1);
        matGraph.addEdge(8, 3, 1);
        matGraph.addEdge(3, 7, 1);
        matGraph.addEdge(7, 8, 1);
        matGraph.addEdge(4, 6, 1);
        matGraph.addEdge(6, 5, 1);
        matGraph.addEdge(5, 4, 1);
        matGraph.addEdge(5, 0, 1);

        System.out.println(matGraph.toGraphviz());

        

    }
}
