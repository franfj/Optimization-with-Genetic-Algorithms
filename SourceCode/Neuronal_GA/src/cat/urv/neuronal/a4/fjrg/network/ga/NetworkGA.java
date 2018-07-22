package cat.urv.neuronal.a4.fjrg.network.ga;

import cat.urv.neuronal.a4.fjrg.ga.GeneticAlgo;
import cat.urv.neuronal.a4.fjrg.network.Network;

import java.util.LinkedList;

public class NetworkGA extends GeneticAlgo {

    private Network actualNetwork;

    public NetworkGA(int nPopulation, Network net) {
        super(nPopulation, net.getVertices().size());
        this.actualNetwork = net;
        this.bestChromosome = new NetworkChromosome(net);

        initialisePopulation();
    }

    @Override
    protected void initialisePopulation() {
        chromosomes = new LinkedList<>();
        for (int i = 0; i < nPopulation; ++i) {
            chromosomes.add(new NetworkChromosome(this.actualNetwork));
        }
    }
}
