package cat.urv.neuronal.a4.fjrg.network.ga;

import cat.urv.neuronal.a4.fjrg.ga.Chromosome;
import cat.urv.neuronal.a4.fjrg.network.Network;

import java.util.LinkedList;
import java.util.List;

public class NetworkChromosome extends Chromosome {

    private Network actualNetwork;

    public NetworkChromosome(Network net) {
        super(net.getVertices().size());
        this.actualNetwork = net;
        this.fitnessScore = 0.0;
        randomiceGenes();
    }

    public NetworkChromosome(Network net, List<Integer> genes) {
        super(net.getVertices().size(), genes);
        this.fitnessScore = 0.0;
    }

    @Override
    public void calculateFitness() {
        this.fitnessScore = actualNetwork.calculateModularity(this);
    }

    @Override
    public Chromosome clone() {
        return new NetworkChromosome(this.actualNetwork);
    }
}
