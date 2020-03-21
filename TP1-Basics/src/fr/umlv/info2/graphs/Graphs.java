package fr.umlv.info2.graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.IntFunction;

public class Graphs {


    /**
     * Parcours en pronfondeur d'un graph :
     * Algo :
     * ---
     * Function DFS : graph , premier sommet v0
     *
     *      Initilisation d'une pile d'entiers pour retourner les sommets dans l'ordre inverse, et de savoir de quel sommet on va chercher le voisin (LIFO)
     *      Initialisation dune liste pour ce souvenir des sommets déja visités (parcours)
     *      Ajout dans la liste du premier sommet passé en paramétre et de ses arrêtes
     *
     *      Tant que la pile n'est pas vide, c'est à dire tant que le parcours n'est pas terminé
     *          On dépile les sommets voisins
     *          Ajout à la liste parcours des sommets voisins dans la liste
     *          Pour chaque arrête issue du sommet parcouru :
     *              On récupére le sommet au bout de l'arrête
     *              si la pile ne contient pas les arrêtes de ce sommet voisin et si  celui-ci n'est pas dans la liste:
     *                  On insére dans la pile
     *              Fin Si
     *          Fin de Boucle
     *      Fin de Boucle
     *      Retourne la liste contenant les sommets du parcours
     * Fin de fonction
     *
     * @param graph
     * @param v0
     * @return liste de mon parcours (sommets visités)
     */
    public static List<Integer> DFS(Graph graph, int v0) {

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
     * Logique du parcours en largeur d'un graph avec une file
     * @param graph Un graph quelconque
     * @param parcours Liste du parcours
     * @param visited  Liste  boolean des sommets parcourus ou non
     * @param queue File
     * @param vertice sommet
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
     * @param graph graph quelconque
     * @param v0   sommet initial
     * @return  liste du parcours
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
     * @param numberOfVertices  nombre de sommets
     * @param numberOfEdges nombre d'arrêtes
     * @param weightMax poids maximal
     * @return un graph aléatoire
     */
    public static Graph createRandomGraph(int numberOfVertices, int numberOfEdges, int weightMax){
        if (numberOfEdges > numberOfVertices + numberOfVertices) { // Si le nombre d'arrêtes est deux fois supérieur au nombre de sommets
            throw new IllegalArgumentException(" Number of edges invalid");
        }
        var graph = new MatGraph(numberOfVertices);
        var rand = new Random();

        for (var i = 0 ; i < numberOfEdges ; i++)
            try {
                var randSrc = rand.nextInt(numberOfVertices);
                var randDst = rand.nextInt(numberOfVertices);
                var randWeight = rand.nextInt(weightMax +  weightMax + 1) -  weightMax;
                graph.addEdge(randSrc, randDst, randWeight);
            } catch (IllegalArgumentException e) {
                i--;
            }
        return graph;
    }


    /**
     * Crée et retourne un graph à partir d'un fichier
     * @param path chemin vers le fichier
     * @return un graph issue d'un fichier
     */
    public static Graph createGraphFromMatrixFile(Path path) throws IOException {
        try {
            var lines = (String[]) Files.lines(path).toArray();
            var numberOfVertices = Integer.parseInt(lines[0]);
            var graph = new MatGraph(numberOfVertices);
            var space = " ";

            for (var i = 0 ; i < numberOfVertices ; i++) {
                var weights = lines[i + 1].split(space);
                for (var j = 0 ; j < numberOfVertices ; j++) {
                    if (j != 0) {
                        graph.addEdge(i, j, Integer.parseInt(weights[j]));
                    }
                }
            }
            return graph;
        } catch (IOException | NumberFormatException e) {
            System.out.println("File error.");
            throw e;
        }
    }


}
