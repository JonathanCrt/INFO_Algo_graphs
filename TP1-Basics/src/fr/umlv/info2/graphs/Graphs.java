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

    /**
     * Parcours en pronfondeur d'un graph :
     * Algo :
     * ---
     * Function DFS : graph , premier sommet v0
     * <p>
     * Initilisation d'une pile d'entiers pour retourner les sommets dans l'ordre inverse, et de savoir de quel sommet on va chercher le voisin (LIFO)
     * Initialisation dune liste pour ce souvenir des sommets déja visités (parcours)
     * Ajout dans la liste du premier sommet passé en paramétre et de ses arrêtes
     * <p>
     * Tant que la pile n'est pas vide, c'est à dire tant que le parcours n'est pas terminé
     * On dépile les sommets voisins
     * Ajout à la liste parcours des sommets voisins dans la liste
     * Pour chaque arrête issue du sommet parcouru :
     * On récupére le sommet au bout de l'arrête
     * si la pile ne contient pas les arrêtes de ce sommet voisin et si  celui-ci n'est pas dans la liste:
     * On insére dans la pile
     * Fin Si
     * Fin de Boucle
     * Fin de Boucle
     * Retourne la liste contenant les sommets du parcours
     * Fin de fonction
     *
     * @param graph
     * @param v0
     * @return liste de mon parcours (sommets visités)
     */
    public static List<Integer> DFSWithStack(Graph graph, int v0) {

        var stack = new Stack<Integer>();
        var parcours = new ArrayList<Integer>();

        parcours.add(v0);
        graph.forEachEdge(v0, edge -> stack.add(edge.getEnd()));

        while (!stack.isEmpty()) {
            var st = stack.pop();
            parcours.add(st);

            graph.forEachEdge(st, edge -> {
                var target = edge.getEnd();
                if (!parcours.contains(target) && !stack.contains(target)) {
                    stack.add(target);
                }
            });
        }
        return parcours;
    }

    /**
     * Méthode récursif Depth First Search
     *
     * @param graph           Un graph quelconque orienté
     * @param vertex          sommet indiqué
     * @param visited         Pour indiquer les sommets déja visités
     * @param parcours        Liste des sommets parcourus
     * @param values          Tableau de valeurs contenant le numéro du sommet et son temps de début de visite et de fin.
     * @param currentTimeStep temps de parcours
     */
    public static void DFS_Rec(Graph graph, int vertex, boolean[] visited, List<Integer> parcours, int[][] values, LongAdder currentTimeStep) {
        if (visited[vertex]) { // if true
            return;
        }
        currentTimeStep.increment();
        visited[vertex] = true; // Le sommet est visité
        parcours.add(vertex); // Ajout au parcours du sommet visité
        values[vertex][START_TIME] = currentTimeStep.intValue(); // Visite du sommet en cours = Bleu
        graph.forEachEdge(vertex, edge -> DFS_Rec(graph, edge.getEnd(), visited, parcours, values, currentTimeStep)); // appel rec.
        currentTimeStep.increment(); //Incrémentation du temps
        values[vertex][END_TIME] = currentTimeStep.intValue(); // Fin de la visite du sommet = Rouge
    }

    /**
     * Méthode DFS avec boucle principale
     *
     * @param graph Un graph quelconque orienté
     * @param v0    sommet initial
     * @return liste contenant les sommet parcourus
     */
    public static List<Integer> DFS(Graph graph, int v0) {
        var numberOfVertices = graph.numberOfVertices();
        var visited = new boolean[numberOfVertices];
        var parcours = new ArrayList<Integer>();
        // TODO : Initialiser tout le tableau de valeurs à -1

        // Boucle principale
        for (var vertex = v0; vertex < numberOfVertices; vertex++) {
            DFS_Rec(graph, vertex, visited, parcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder()); // chaque case du tableau est un tableau de 2 cases (pour les temps)
        }
        for (var vertex = 0; vertex < v0; vertex++) {
            DFS_Rec(graph, vertex, visited, parcours, new int[numberOfVertices][TIMES_VERTEX], new LongAdder());
        }
        return parcours;
    }


    /**
     * Logique du parcours en largeur d'un graph avec une file
     *
     * @param graph    Un graph quelconque
     * @param parcours Liste du parcours
     * @param visited  Liste  boolean des sommets parcourus ou non
     * @param queue    File
     * @param vertice  sommet
     */
    private static void doBFS(Graph graph, ArrayList<Integer> parcours, boolean[] visited, ArrayDeque<Integer> queue, int vertice) {
        queue.offer(vertice); // Ajout du sommet à la fin de la file (il fait la queue...)
        while (!queue.isEmpty()) { // Tant que la file n'est pas vide
            var vertex = queue.poll(); // On enlève le sommet en tête de file
            if (!visited[vertex]) { // Si le tableau des booleans ne contient pas le sommet à l'index (false)
                visited[vertex] = true; // Le sommet est visité
                parcours.add(vertex); // Ajout à la liste parcours du sommet visité
                graph.forEachEdge(vertex, edge -> queue.offer(edge.getEnd())); // Pour chaque arrêt du sommet on ajout le sommet au bout de l'arrêt à la file
            }
        }
    }

    /**
     * Parcours en largeur d'un graph avec une file
     *
     * @param graph graph quelconque
     * @param v0    sommet initial
     * @return liste du parcours
     */
    public static List<Integer> BFS(Graph graph, int v0) {
        var numberOfVertices = graph.numberOfVertices(); // Nombre de sommets du graph
        var queue = new ArrayDeque<Integer>(numberOfVertices); // File
        var parcours = new ArrayList<Integer>(); // Liste parcorus avec les sommets visité
        var visited = new boolean[numberOfVertices]; // Tableau de  boolean pour indiquer  les sommets visités

        var vertex = v0;
        while (vertex < numberOfVertices) { // Tant que le sommet est inférieur au nombre total de sommet
            doBFS(graph, parcours, visited, queue, vertex);
            vertex++;
        }
        while (vertex < v0) { // Tant que le sommet est inférieur au sommet initial (parcours non terminé)
            doBFS(graph, parcours, visited, queue, vertex);
            vertex++;
        }
        return parcours;
    }


    /**
     * Crée et retourne un graph aléatoire avec un nombre de sommets ert d'arêtes déinies en paramétres
     *
     * @param numberOfVertices nombre de sommets
     * @param numberOfEdges    nombre d'arrêtes
     * @param weightMax        poids maximal
     * @return un graph aléatoire
     */
    public static Graph createRandomGraph(int numberOfVertices, int numberOfEdges, int weightMax) {
        if (numberOfEdges > numberOfVertices + numberOfVertices) { // Si le nombre d'arrêtes est deux fois supérieur au nombre de sommets
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
     * Crée et retourne un graph à partir d'un fichier
     *
     * @param path chemin vers le fichier
     * @return un graph issue d'un fichier
     */
    public static Graph createGraphFromMatrixFile(Path path) throws IOException {
        var lines = Files.readAllLines(path); // read the lines of the file
        var vertices = Integer.parseInt(lines.get(0)); // get the number of vertices
        var graph = lines.subList(1, lines.size()); // get the matrix that represents the graph

        var arcs = graph
                .stream()
                // chaque ligne devient une liste splitée
                .map(line -> Arrays
                        .asList(line.split(" "))
                        .stream()
                        // chaque element de la liste converti en int
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

        for (var vertex = v0; vertex < numberOfVertices; vertex++) {
            DFS_Rec(graph, vertex, visited, new LinkedList<>(), arrayOfVerticesTimed, currentTimeStep);
        }
        for (var vertex = 0; vertex < v0; vertex++) {
            DFS_Rec(graph, vertex, visited, new LinkedList<>(), arrayOfVerticesTimed, currentTimeStep);
        }
        System.out.println("Array of vertices timed : " + Arrays.deepToString(arrayOfVerticesTimed));
        return arrayOfVerticesTimed;
    }


}
