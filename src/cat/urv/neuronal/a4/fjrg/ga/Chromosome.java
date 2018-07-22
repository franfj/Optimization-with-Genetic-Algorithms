package cat.urv.neuronal.a4.fjrg.ga;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class Chromosome {

    protected int nGenes;
    protected List<Integer> genes;
    protected double fitnessScore;

    public Chromosome(int nGenes) {
        this.nGenes = nGenes;
        this.fitnessScore = 0.0;
        randomiceGenes();
    }

    public Chromosome(int nGenes, List<Integer> genes) {
        this.nGenes = nGenes;
        this.genes = genes;
        this.fitnessScore = 0.0;
    }

    public void flipGene(int index) {
        if (this.genes.get(index) == 0) {
            this.genes.set(index, 1);
        } else {
            this.genes.set(index, 0);
        }

        calculateFitness();
    }

    protected void randomiceGenes() {
        genes = new LinkedList<>();

        Random rand = new Random();
        for (int i = 0; i < nGenes; ++i) {
            genes.add(Math.abs(rand.nextInt() % 2));
        }
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public void setGenes(List<Integer> genes) {
        this.genes = genes;
        calculateFitness();
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public abstract void calculateFitness();


//    public void calculateFitness() {
//        // FIXME: Dummy fitness function
//
//        double fitnessScore = 0.0;
//        for (int i = 0; i < this.genes.size(); ++i) {
//            fitnessScore += this.genes.get(i);
//        }
//
//        this.fitnessScore = fitnessScore;
//    }

    public abstract Chromosome clone();

    @Override
    public String toString() {
        return "Chromosome{" +
                "fitnessScore=" + fitnessScore +
                ", genes=" + genes +
                '}';
    }
}
