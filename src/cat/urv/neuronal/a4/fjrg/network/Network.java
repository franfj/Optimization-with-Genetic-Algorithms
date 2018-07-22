package cat.urv.neuronal.a4.fjrg.network;

import cat.urv.neuronal.a4.fjrg.ga.Chromosome;
import cat.urv.neuronal.a4.fjrg.network.ga.NetworkChromosome;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.LinkedList;
import java.util.List;

public class Network {

    private List<Integer> vertices;
    private Multimap<Integer, Integer> edges;
    private Multimap<Integer, Integer> invertedEdges;

    public Network() {
        this.vertices = new LinkedList<>();
        this.edges = LinkedListMultimap.create();
    }

    public void finishConstruction() {
        this.invertedEdges = Multimaps.invertFrom(edges, ArrayListMultimap.<Integer, Integer>create());
    }

    public List<Integer> getVertices() {
        return vertices;
    }

    public Multimap<Integer, Integer> getEdges() {
        return edges;
    }

    public void addVertex(int v) {
        this.vertices.add(v);
    }

    public void addEdge(int a, int b) {
        this.edges.put(a, b);
    }

    public double calculateModularity(Chromosome solution) {
        double modularity = 1.0 / (2.0 * getNumberOfEdges());
        double aux = 0.0;

        for (int i = 1; i <= vertices.size(); ++i) {
            for (int j = 1; j <= vertices.size(); ++j) {
                aux += (connection(i, j) - ((calculateDegree(i) * calculateDegree(j)) / (2.0 * getNumberOfEdges())))
                        * calculateDelta(solution.getGenes().get(i - 1), solution.getGenes().get(j - 1));
            }
        }

        modularity *= aux;

        return modularity;
    }

    private int calculateDelta(int geneA, int geneB) {
        return geneA == geneB ? 1 : 0;
    }

    private int calculateDegree(int vertex) {
        int degree = 0;

        degree += edges.get(vertex).size();
        degree += invertedEdges.get(vertex).size();

        return degree;
    }

    private int connection(int vertexA, int vertexB) {
        if (edges.get(vertexA) != null) {
            for (Integer v : edges.get(vertexA)) {
                if (v == vertexB) {
                    return 1;
                }
            }
        }

        if (invertedEdges.get(vertexA) != null) {
            for (Integer v : invertedEdges.get(vertexA)) {
                if (v == vertexB) {
                    return 1;
                }
            }
        }

        return 0;
    }

    private int getNumberOfEdges() {
        int aux = 0;
        for (int i = 0; i < vertices.size(); ++i) {
            if (edges.get(i) != null) {
                aux += edges.get(i).size();
            }
        }

        return aux;
    }

}
