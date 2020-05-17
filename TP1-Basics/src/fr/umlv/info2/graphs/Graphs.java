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
        var numberOfVertices = graph.numberOfVertices();
        var visited = new boolean[numberOfVertices];
        var parcours = new ArrayList<Integer>();

        // main loop
        for (var vertex = v0; vertex < numberOfVertices; vertex++) {
            DFS_Rec(graph, vertex, visited, parcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder()); // Each array is an array of 2 boxes (for time)
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
        var visited = new boolean[numberOfVertices]; // Tableau de  boolean pour indiquer  les sommets visités

        var vertex = v0;
        while (vertex < numberOfVertices) { // As long as the top is less than the total number of top
            doBFS(graph, parcours, visited, queue, vertex);
            vertex++;
        }
        while (vertex < v0) { // As long as the top is lower than the initial vertex (path not completed)
            doBFS(graph, parcours, visited, queue, vertex);
            vertex++;
        }
        return parcours;
    }


    /**
     * Creates and returns a random graph with a number of green vertices defined in parameters
     *
     * @param numberOfVertices number of vertices
     * @param numberOfEdges    number of edges
     * @param weightMax        max. weight
     * @return random graph
     */
    public static Graph createRandomGraph(int numberOfVertices, int numberOfEdges, int weightMax) {
        if (numberOfEdges > numberOfVertices + numberOfVertices) { // If the number of edges is twice the number of vertices
            throw new IllegalArgumentException(" Number of edges invalid");
        }
        var graph = new MatGraph(numberOfVertices);
        var rand = new Random();

        for (var i = 0; i < numberOfEdges; i++)
            try {
                var randSrc = rand.nextInt(numberOfVertices);
                var randDst = rand.nextInt(numberOfVertices);
                var randWeight = rand.nextInt(weightMax + weightMax + 1) - weightMax;
                graph.addEdge(randSrc, randDst, randWeight);
            } catch (IllegalArgumentException e) {
                i--;
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

        var arcs = graph
                .stream()
                // each row becomes a splited list
                .map(line -> Arrays
                        .asList(line.split(" "))
                        .stream()
                        // each element 
                        .map(Integer::parseInt)
                        // converti en liste
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        var g = new MatGraph(vertices);
        for (var i = 0; i < arcs.size(); i++) {
            var l = arcs.get(i);
            for (var j = 0; j < l.size(); j++) {
                var ll = l.get(j);
                if (ll != 0)
                    g.addEdge(i, j, ll);
            }
        }
        return g;
    }

    /**
     * Méthode permettant le parcours en profondeur 'timé'
     *
     * @param graph Un graphe orienté
     * @param v0    sommet initial
     * @return Tableau contenant les sommets (index) et les temps de début de visite et de fin.
     */
    public static int[][] timedDepthFirstSearch(Graph graph, int v0) {
        var numberOfVertices = graph.numberOfVertices();
        var visited = new boolean[numberOfVertices];
        var arrayOfVerticesTimed = new int[numberOfVertices][TIMES_VERTEX];
        var currentTimeStep = new LongAdder(); // to make sure thread safe
        currentTimeStep.decrement();
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

    public static List<Integer> topologicalSort(Graph g, boolean cycleDetect) {
        var numberOfVertices = g.numberOfVertices();
        var visited = new boolean[numberOfVertices];

        var parcours = new Stack<Integer>();

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
     * @param g
     * @param vertex
     * @param visited
     * @param parcours
     */
    public static void topologicalSortRec(Graph g, int vertex, boolean[] visited, Stack<Integer> parcours, List<Integer> ancestorsVertices, boolean cycleDetect) {
        visited[vertex] = true;
        ancestorsVertices.add(vertex);

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
     * implementation de l'algorithme de kosaraju
     *
     * @param g graph donné
     * @return Composantes fortement connexe
     */
    public static List<List<Integer>> scc(Graph g) {
        var kosarajuParcours = new ArrayList<List<Integer>>();

        var vertices = topologicalSort(g, false);
        var numberOfVertices = vertices.size();
        var transposedGraph = g.transpose();
        var visited = new boolean[numberOfVertices];

        for (var vertex : vertices) {
            if (!visited[vertex]) {
                var secondParcours = new ArrayList<Integer>();
                DFS_Rec(transposedGraph, vertex, visited, secondParcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder());
                kosarajuParcours.add(secondParcours);
            }
        }
        return kosarajuParcours;
    }

    /**
     * implementation de l'algorithme de bellman-ford
     *
     * @param g      graph donnée
     * @param source sommet de départ
     * @return plus court chemin depuis le sommet de départ
     */
    public static ShortestPathFromOneVertex bellmanFord(Graph g, int source) {
        var d = new int[g.numberOfVertices()];
        var pi = new Integer[g.numberOfVertices()];
        var numberOfVertices = g.numberOfVertices();

        for (var index = 0; index < numberOfVertices; index++) {
            d[index] = PLUS_INFINITE;
            pi[index] = PLUS_INFINITE;
        }
        d[source] = 0;
        pi[source] = source;

        for (var i = 0; i < numberOfVertices; i++) {
            for (var vertex = 0; vertex < numberOfVertices; vertex++) {
                g.forEachEdge(vertex, edge -> {
                    var weight = edge.getValue();
                    var target = edge.getEnd();

                    if (d[target] > d[edge.getStart()] + weight) {
                        d[target] = d[edge.getStart()] + weight;
                        pi[target] = edge.getStart();
                    }
                });
            }
        }
        for (var vertex = 0; vertex < g.numberOfVertices(); vertex++) {
            g.forEachEdge(vertex, edge -> {
                var weight = edge.getValue();
                var target = edge.getEnd();
                if (d[target] > d[edge.getStart()] + weight) {
                    throw new IllegalArgumentException("Error: Graph given have negative circle");
                }

            });
        }
        return new ShortestPathFromOneVertex(source, d, pi);
    }

    /**
     * implementation de l'algorithme de dijktra
     *
     * @param g      graph donnée
     * @param source sommet de départ
     * @return plus court chemin depuis le sommet de départ
     */
    public static ShortestPathFromOneVertex dijkstra(Graph g, int source) {

        var numberOfVertices = g.numberOfVertices();
        var d = new int[numberOfVertices];
        var pi = new Integer[numberOfVertices];
        var todoVertices = new PriorityQueue<Integer>();

        // intialization of d and pi for each vertices
        for (var s = 0; s < numberOfVertices; s++) {
            d[s] = PLUS_INFINITE;
            pi[s] = null;
            todoVertices.add(s);
        }
        d[source] = 0;
        while (!todoVertices.isEmpty()) {

            var s = todoVertices.poll();
            g.forEachEdge(s, edge ->
            {
                var target = edge.getEnd();
                var weight = edge.getValue();
                if (d[s] + weight < d[target]) {
                    d[target] = d[s] + weight;
                    pi[target] = s;
                }
            });
        }
        return new ShortestPathFromOneVertex(source, d, pi);
    }


    public static ShortestPathFromAllVertices floydWarshall(Graph g) {
        var numberOfVertices = g.numberOfVertices();
        var d = new int[numberOfVertices][numberOfVertices];
        var pi = new int[numberOfVertices][numberOfVertices];

        for (var s = 0; s < numberOfVertices; s++) {
            for (var t = 0; t < numberOfVertices; t++) {
                if (s == t & g.isEdge(s, t)) {
                    d[s][t] = 0;
                    pi[s][t] = -1;
                } else if (g.isEdge(s, t)) {
                    d[s][t] = g.getWeight(s, t);
                    pi[s][t] = s;
                } else {
                    d[s][t] = PLUS_INFINITE;
                    pi[s][t] = -1;
                }

            }
        }

        for (var k = 0; k < numberOfVertices - 1; k++) {
            for (var s = 0; s < numberOfVertices; s++) {
                for (var t = 0; t < numberOfVertices; t++) {
                    if (d[s][t] > d[s][k] + d[k][t]) {
                        d[s][t] = d[s][k] + d[k][t];
                        pi[s][t] = pi[k][t];
                    }
                }
            }
        }
        return new ShortestPathFromAllVertices(d, pi);

    }
}
