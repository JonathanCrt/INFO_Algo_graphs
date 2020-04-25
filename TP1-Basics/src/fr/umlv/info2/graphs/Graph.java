package fr.umlv.info2.graphs;

import java.util.Iterator;
import java.util.function.Consumer;

public interface Graph {

    /**
     * Retourne le nombre d'arêtes du graph
     *
     * @return le nombre d'arêtes du graph
     */
    int numberOfEdges();

    /**
     * Retourne le nombre de sommets du graph
     *
     * @return le nombre de sommets du graph
     */
    int numberOfVertices();

    /**
     * Permet d'ajouter une arête orientée au graph
     *
     * @param i     la 1ère extremité de l'arête
     * @param j     la 2ème extremité de l'arête
     * @param value le poids de l'arête
     */
    void addEdge(int i, int j, int value);

    /**
     * Teste l'existence d'une arête donnée
     *
     * @param i la 1ère extremité de l'arête
     * @param j la 2ème extremité de l'arête
     * @return true s'il existe une arête entre i et j; false sinon
     */
    boolean isEdge(int i, int j);

    /**
     * Retourne le poids d'une arête donné
     *
     * @param i la 1ère extremité de l'arête
     * @param j la 2ème extremité de l'arête
     * @return Le poids de l'arête entre i et j
     */
    int getWeight(int i, int j);

    /**
     * Renvoie un itérateur sur tous les voisins d'un sommet donné.
     *
     * @param i le sommet à partir duquel partent les arêtes fournies par l'itérateur
     * @return un itérateur sur tous les voisins du sommet i
     */
    Iterator<Edge> edgeIterator(int i);

    /**
     * Effectue une action sur tous les arêtes d'un sommet donné.
     *
     * @param i        le sommet à partir duquel partent les arêtes traitées
     * @param consumer l'action effectuée sur toutes les arêtes voisines de i
     */
    void forEachEdge(int i, Consumer<Edge> consumer);

    /**
     * Affiche le graph
     *
     * @return affichage du graph
     */
    default String toGraphviz() {
        var sb = new StringBuilder();
        sb.append("digraph  {").append(System.lineSeparator());

        for (var i = 0; i < this.numberOfVertices(); i++) {
            sb.append(i + ";").append(System.lineSeparator());

            this.edgeIterator(i).forEachRemaining(
                    edge -> {
                        var label = "[ label=\"" + edge.getValue() + "\" ]";
                        sb.append(edge.getStart() + " -> " + edge.getEnd() + " " + label + " ;")
                                .append(System.lineSeparator());
                    }
            );
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Inverse les arrêtes du graphe
     * @return graph transposé
     */
    default Graph transpose() {
        var graph = this;
        var numberOfVertices = graph.numberOfVertices();
        var matGraph = new MatGraph(numberOfVertices);

        for (var vertex = 0; vertex < numberOfVertices; vertex++) {
            graph.forEachEdge(vertex, edge -> matGraph.addEdge(edge.getEnd(), edge.getStart(), edge.getValue()));
        }
        return matGraph;
    }
}
