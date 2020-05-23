package fr.umlv.info2.graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class Graphs {

    public static int START_TIME = 0;
    public static int END_TIME = 1;
    public static int TIMES_VERTEX = 2;
    public static int PLUS_INFINITE = Integer.MAX_VALUE;

    /**
     * Recursive method Depth First Search
     *
     * @param graph           any graph oriented
     * @param vertex          indicated vertex
     * @param visited         array of boolean to indicate vertices already visited
     * @param parcours        resulting list (vertices)
     * @param values          A table of values containing the vertex number and its start and end time.
     * @param currentTimeStep temps de parcours
     */
    public static void DFS_Rec(Graph graph, int vertex, boolean[] visited, List<Integer> parcours, int[][] values, LongAdder currentTimeStep) {
        if (visited[vertex]) { // if true
            return;
        }
        currentTimeStep.increment();
        visited[vertex] = true; // vertex is visited
        parcours.add(vertex); // Added to the path of the visited vertices
        values[vertex][START_TIME] = currentTimeStep.intValue(); // Ongoing vertex visit
        graph.forEachEdge(vertex, edge -> DFS_Rec(graph, edge.getEnd(), visited, parcours, values, currentTimeStep)); // rec. call
        currentTimeStep.increment(); //Time increment
        values[vertex][END_TIME] = currentTimeStep.intValue(); // End of vertex visit
    }

    /**
     * DFS method with main loop
     *
     * @param graph any graph oriented
     * @param v0    initial vertex
     * @return list containing visited vertices
     * improvement : initialize all values of vertices array with -1
     */
    public static List<Integer> DFS(Graph graph, int v0) {
        var numberOfVertices = graph.numberOfVertices(); // get number of vertices of graph
        var visited = new boolean[numberOfVertices]; // array of boolean to indicate visited vertices
        var parcours = new ArrayList<Integer>(); // list containing visited vertices

        // main loop
        for (var vertex = v0; vertex < numberOfVertices; vertex++) {
            // Each array is an array of 2 boxes (for time)
            DFS_Rec(graph, vertex, visited, parcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder());
        }
        for (var vertex = 0; vertex < v0; vertex++) {
            DFS_Rec(graph, vertex, visited, parcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder());
        }
        return parcours;
    }


    /**
     * Logic of the BFS
     *
     * @param graph    any graph
     * @param parcours list containing visited vertices
     * @param visited  Boolean List of visited or Non-Visited vertices
     * @param queue    queue
     * @param vertice  vertex
     */
    private static void doBFS(Graph graph, ArrayList<Integer> parcours, boolean[] visited, ArrayDeque<Integer> queue, int vertice) {
        queue.offer(vertice); // Added the top at the end of the queue (it's queuing..)
        while (!queue.isEmpty()) { // As long as the queue is not empty
            var vertex = queue.poll(); // We take the top out of queue
            if (!visited[vertex]) { // If the booleans array does not contain the top at the index (false)
                visited[vertex] = true; // vertex is visited
                parcours.add(vertex); // Adding to the path list of the visited vertices
                graph.forEachEdge(vertex, edge -> queue.offer(edge.getEnd())); // For each stop of the vertex one adds the vertex at the end of the stop to the queue
            }
        }
    }

    /**
     * BFS method
     *
     * @param graph any graph
     * @param v0    sommet initial
     * @return path list
     */
    public static List<Integer> BFS(Graph graph, int v0) {
        var numberOfVertices = graph.numberOfVertices(); // number of vertices (graph)
        var queue = new ArrayDeque<Integer>(numberOfVertices); // queue
        var parcours = new ArrayList<Integer>(); // path list
        var visited = new boolean[numberOfVertices]; //Table of boolean to indicate visited vertices

        var vertex = v0;
        while (vertex < numberOfVertices) { // As long as the vertex is less than the total number of top
            doBFS(graph, parcours, visited, queue, vertex);
            vertex++;
        }
        while (vertex < v0) { // As long as the vertexis lower than the initial vertex (path not completed)
            doBFS(graph, parcours, visited, queue, vertex);
            vertex++;
        }
        return parcours;
    }


    /**
     * Creates and returns a random graph with a number of edges, vertices defined in parameters
     *
     * @param numberOfVertices number of vertices
     * @param numberOfEdges    number of edges
     * @param weightMax        max. weight
     * @return random graph
     */
    public static Graph createRandomGraph(int numberOfVertices, int numberOfEdges, int weightMax) {
        if (numberOfVertices + numberOfVertices < numberOfEdges) { // If the number of edges is twice the number of vertices
            throw new IllegalArgumentException("Error: number of edges invalid");
        }
        var graph = new MatGraph(numberOfVertices);
        var rand = new Random();

        var i = 0;
        while( i < numberOfEdges) {
            try {
                var randSrc = rand.nextInt(numberOfVertices); // create random source
                var randDst = rand.nextInt(numberOfVertices); // create random destination
                var randWeight = rand.nextInt(weightMax + weightMax + 1) - weightMax; // create random weight
                graph.addEdge(randSrc, randDst, randWeight);  // add randome dge to graph
            } catch (IllegalArgumentException e) {
                i--;
            }
            i++;
        }
        return graph;
    }


    /**
     * Creates and returns a graph from a file
     *
     * @param path path to file
     * @return a graph from a file
     */
    public static Graph createGraphFromMatrixFile(Path path) throws IOException {
        var lines = Files.readAllLines(path); // read the lines of the file
        var vertices = Integer.parseInt(lines.get(0)); // get the number of vertices
        var graph = lines.subList(1, lines.size()); // get the matrix that represents the graph

        var edges = graph
                .stream()
                // each row becomes a splited list
                .map(line -> Arrays.stream(line.split(" "))
                        // each element
                        .map(Integer::parseInt)
                        // parse to list
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        var matGraph = new MatGraph(vertices);
        var i = 0;
        while(i < edges.size()) {
            var edgeList = edges.get(i);
            for (var j = 0; j < edgeList.size(); j++) {
                var weight = edgeList.get(j);
                if (weight != 0) {
                    matGraph.addEdge(i, j, weight);
                }
            }
            i++;
        }
        return matGraph;
    }

    /**
     * DFS timed version
     *
     * @param graph any oriented graph
     * @param v0    initial vertex
     * @return An array of values containing the vertex number and its start and end time.
     */
    public static int[][] timedDepthFirstSearch(Graph graph, int v0) {
        var numberOfVertices = graph.numberOfVertices(); // get number of vertices of graph
        var visited = new boolean[numberOfVertices];

        // Create an array of values containing the vertex number and its start and end time.
        var arrayOfVerticesTimed = new int[numberOfVertices][TIMES_VERTEX];
        var currentTimeStep = new LongAdder(); // to make sure thread safe
        currentTimeStep.decrement();
        // do DFS on graph
        for (var vertex = v0; vertex < numberOfVertices; vertex++) {
            DFS_Rec(graph, vertex, visited, new LinkedList<>(), arrayOfVerticesTimed, currentTimeStep);
        }
        for (var vertex = 0; vertex < v0; vertex++) {
            DFS_Rec(graph, vertex, visited, new LinkedList<>(), arrayOfVerticesTimed, currentTimeStep);
        }
        System.out.println("Array of vertices timed : " + Arrays.deepToString(arrayOfVerticesTimed));
        return arrayOfVerticesTimed;
    }

    /*
    public static List<Integer> topologicalSort(Graph g) {
        return topologicalSort(g, () -> {});
    }
     */

    /**
     * Main topological sort method with detection of negative circle
     *
     * @param g           any graph
     * @param cycleDetect to active detection of negative circle or not
     * @return An array containing result(vertices) of topological sort
     */
    public static List<Integer> topologicalSort(Graph g, boolean cycleDetect) {
        var numberOfVertices = g.numberOfVertices();
        var visited = new boolean[numberOfVertices];

        var parcours = new Stack<Integer>(); // to find a vertex with no requirements and remove it

        // for each vertex of graph do
        for (var vertex = 0; vertex < numberOfVertices; vertex++) {
            if (!visited[vertex]) {
                topologicalSortRec(g, vertex, visited, parcours, new ArrayList<>(), cycleDetect);
            }
        }
        var result = new ArrayList<Integer>();
        while (!parcours.isEmpty()) {
            var t = parcours.pop();
            result.add(t);
        }

        return result;
    }


    /**
     * recursive topological sort method
     *
     * @param g                 any graph
     * @param vertex            given vertex
     * @param visited           boolean List of visited or Non-Visited vertices
     * @param parcours          process
     * @param ancestorsVertices list of ancetors
     * @param cycleDetect       detection of negative circle
     */
    public static void topologicalSortRec(Graph g, int vertex, boolean[] visited, Stack<Integer> parcours, List<Integer> ancestorsVertices, boolean cycleDetect) {
        visited[vertex] = true;
        ancestorsVertices.add(vertex);

        // for each vertex of g do
        g.forEachEdge(vertex, v -> {
            if (!visited[v.getEnd()]) {
                topologicalSortRec(g, v.getEnd(), visited, parcours, ancestorsVertices, cycleDetect);
            } else {
                if (ancestorsVertices.contains(v.getEnd())) {
                    if (cycleDetect) {
                        throw new IllegalStateException("Warning : The graph have a cycle");
                    }
                }
            }
        });

        System.out.println(ancestorsVertices);
        ancestorsVertices.remove(Integer.valueOf(vertex));
        parcours.push(vertex);
    }

    /**
     * kosaraju method
     *
     * @param g any graph
     * @return strongly connected component
     */
    public static List<List<Integer>> scc(Graph g) {
        var kosarajuParcours = new ArrayList<List<Integer>>(); // to store result sets

        // do topological sort to create list with ordered vertices (by decreasing dates)
        var vertices = topologicalSort(g, false);
        var numberOfVertices = vertices.size();
        var transposedGraph = g.transpose(); // calculate transposed graph (inverse directions of edges)
        var visited = new boolean[numberOfVertices]; // array of visited vertices

        // main loop
        for (var vertex : vertices) {
            if (!visited[vertex]) {
                var secondParcours = new ArrayList<Integer>();
                // call DFS recursive method with transposed graph
                DFS_Rec(transposedGraph, vertex, visited, secondParcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder());
                kosarajuParcours.add(secondParcours);
            }
        }
        return kosarajuParcours;
    }

    /**
     * bellman-ford method
     *
     * @param g      any graph
     * @param source source vertex
     * @return shortest path from the starting vertex
     */
    public static ShortestPathFromOneVertex bellmanFord(Graph g, int source) {
        var d = new int[g.numberOfVertices()]; // to store all distances
        var pi = new Integer[g.numberOfVertices()]; // to store all predecessors
        var numberOfVertices = g.numberOfVertices(); // to get number of vertices of graph

        // initialization
        for (var index = 0; index < numberOfVertices; index++) {
            d[index] = PLUS_INFINITE;
            pi[index] = PLUS_INFINITE;
        }
        d[source] = 0;
        pi[source] = source;

        // we search a more effective path length
        for (var i = 0; i < numberOfVertices; i++) {
            for (var vertex = 0; vertex < numberOfVertices; vertex++) {
                g.forEachEdge(vertex, edge -> {
                    var weight = edge.getValue();
                    var target = edge.getEnd();
                    // to find a better path
                    if (d[target] > d[edge.getStart()] + weight) {
                        d[target] = d[edge.getStart()] + weight;
                        pi[target] = edge.getStart();
                    }
                });
            }
        }

        // to detect negative circle
        for (var vertex = 0; vertex < g.numberOfVertices(); vertex++) {
            g.forEachEdge(vertex, edge -> {
                var weight = edge.getValue(); // get weight of edge
                var target = edge.getEnd(); // get vertex at the end of edge
                if (d[target] > d[edge.getStart()] + weight) {
                    throw new IllegalArgumentException("Error: Graph given have negative circle");
                }

            });
        }
        return new ShortestPathFromOneVertex(source, d, pi);
    }

    /**
     * dijktra method
     *
     * @param g      any graph
     * @param source source vertex
     * @return shortest path from the starting vertex
     */
    public static ShortestPathFromOneVertex dijkstra(Graph g, int source) {

        var numberOfVertices = g.numberOfVertices(); // to get number of vertices of graph
        var d = new int[numberOfVertices]; // to store all distances
        var pi = new Integer[numberOfVertices]; // to store all predecessors
        var todoVertices = new PriorityQueue<Integer>(); // todoset of vertices to be processed

        // intialization of d and pi for each vertices
        for (var s = 0; s < numberOfVertices; s++) {
            d[s] = PLUS_INFINITE;
            pi[s] = null;
            todoVertices.add(s);
        }
        d[source] = 0;
        while (!todoVertices.isEmpty()) {

            var s = todoVertices.poll(); // extract of todoVertices that which minimizes d
            // for each edge from source to target do
            g.forEachEdge(s, edge ->
            {
                var target = edge.getEnd(); // get vertex at the end of edge
                var weight = edge.getValue(); // get weight of edge
                //  to find a better path
                if (d[s] + weight < d[target]) {
                    d[target] = d[s] + weight;
                    pi[target] = s;
                }
            });
        }
        return new ShortestPathFromOneVertex(source, d, pi); // return shortest path
    }

    /**
     * floyd-Warshall method
     *
     * @param g any graph
     * @return shortest path from the starting vertex
     */
    public static ShortestPathFromAllVertices floydWarshall(Graph g) {
        var numberOfVertices = g.numberOfVertices(); // to get number of vertices of graph
        var d = new int[numberOfVertices][numberOfVertices]; // d matrix of weights of all shortest paths
        var pi = new int[numberOfVertices][numberOfVertices]; // pi matrix of predecessors

        // initialization
        for (var s = 0; s < numberOfVertices; s++) {
            for (var t = 0; t < numberOfVertices; t++) {
                if (s == t) {
                    d[s][t] = 0;
                } else {
                    d[s][t] = 1_000_000; // "equivalent" to plus infinity

                }
                pi[s][t] = -1;
            }
        }
        // for s to t  of g do
        for (int s = 0; s < numberOfVertices; s++) {
            for (int t = 0; t < numberOfVertices; t++) {
                if (g.isEdge(s, t)) {
                    if (s != t) {
                        d[s][t] = g.getWeight(s, t); // weight = weight of edge from s to t
                        pi[s][t] = s; // predecessor = source
                    }
                }
            }
        }

        // main loop : paths with intermediate vertices < k + 1 is calculated
        for (var k = 0; k < numberOfVertices - 1; k++) {
            for (var s = 0; s < numberOfVertices; s++) {
                for (var t = 0; t < numberOfVertices; t++) {
                    if (d[s][t] > d[s][k] + d[k][t]) {
                        d[s][t] = d[s][k] + d[k][t]; // actual weight of shortest path from s to t
                        pi[s][t] = pi[k][t];
                    }
                }
            }
        }
        return new ShortestPathFromAllVertices(d, pi); // return shortest path
    }
}
