package cat.urv.neuronal.a4.fjrg.ga;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class GeneticAlgo {

    protected int SELECTIONS_PER_GENERATION = 10;
    protected double CROSS_PROBABILITY = 0.5;
    protected double MUTATION_PROBABILITY = 0.01;

    protected int nPopulation;
    protected int nGenesPerChromosome;
    protected List<Chromosome> chromosomes;

    protected Chromosome bestChromosome;

    private String saveFile;

    public GeneticAlgo(int nPopulation, int nGenesPerChromosome) {
        this.nPopulation = nPopulation;
        this.nGenesPerChromosome = nGenesPerChromosome;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            // EVALUATION
            evaluatePopulation();

            if (getBestChromosome(chromosomes).getFitnessScore() > bestChromosome.getFitnessScore()) {
                bestChromosome = getBestChromosome(chromosomes);
            }

            // SELECTION
            List<Chromosome> selectedChromosomes = new LinkedList<>();
            selectPopulationWithRank(selectedChromosomes);
            chromosomes.clear();

            //CROSSOVER
            List<Chromosome> crossedChromosomes = new LinkedList<>();
            crossPopulation(selectedChromosomes, crossedChromosomes);

            //MUTATION
            mutatePopulation(crossedChromosomes);

//            if (i % 100 == 0) {
//                System.out.println(bestChromosome);
//            }
        }

        System.out.println("Best solution found:\n" + bestChromosome);
        saveSolutionToClu();

    }

    protected abstract void initialisePopulation();

    // Roulette-Wheel selection method
    protected void evaluatePopulation() {
        for (Chromosome chromosomeToEvaluate : chromosomes) {
            chromosomeToEvaluate.calculateFitness();
        }
        Collections.sort(chromosomes, Comparator.comparingDouble(Chromosome::getFitnessScore).reversed());
    }

    // Rank as fitness function
    protected void selectPopulationWithRank(List<Chromosome> selectedChromosomes) {
        this.chromosomes = this.chromosomes.subList(0, nPopulation / 4);

        List<Integer> ranks = new LinkedList<>();
        int firstRank = chromosomes.size();
        for(Chromosome chromosome: chromosomes){
            ranks.add(firstRank--);
        }

        // Calculate total fitness
        double totalFitness = 0;
        for (Integer rank : ranks) {
            totalFitness += rank;
        }

        for (int k = 0; k < SELECTIONS_PER_GENERATION; ++k) {
            // Selection
            double valueSelected = new Random().nextDouble() * totalFitness;
            for (int i = 0; i < chromosomes.size(); i++) {
                valueSelected -= ranks.get(i);

                if (valueSelected <= 0) {
                    selectedChromosomes.add(chromosomes.get(i));
                    break;
                }
            }
        }
    }

    // Modularity as fitness function
    protected void selectPopulation(List<Chromosome> selectedChromosomes) {
        this.chromosomes = this.chromosomes.subList(0, nPopulation / 4);

        // Calculate total fitness
        double totalFitness = 0;
        for (Chromosome chromosome : chromosomes) {
            totalFitness += chromosome.getFitnessScore();
        }

        for (int k = 0; k < SELECTIONS_PER_GENERATION; ++k) {
            // Selection
            double valueSelected = new Random().nextDouble() * totalFitness;
            for (int i = 0; i < chromosomes.size(); i++) {
                valueSelected -= chromosomes.get(i).getFitnessScore();

                if (valueSelected <= 0) {
                    selectedChromosomes.add(chromosomes.get(i));
                    break;
                }
            }
        }
    }

    protected void crossPopulation(List<Chromosome> selectedChromosomes, List<Chromosome> crossedChromosomes) {
        while (crossedChromosomes.size() < nPopulation) {
            Random rand = new Random();

            int randomElementIndex = rand.nextInt(selectedChromosomes.size());
            Chromosome firstCrossChromosome = selectedChromosomes.get(randomElementIndex);

            if (crossedChromosomes.size() == nPopulation - 1) {
                crossedChromosomes.add(firstCrossChromosome);
            } else {
                randomElementIndex = rand.nextInt(selectedChromosomes.size());
                Chromosome secondCrossChromosome = selectedChromosomes.get(randomElementIndex);

                crossChromosomesSP(crossedChromosomes, firstCrossChromosome, secondCrossChromosome);
            }
        }
    }

    // Single Point crossover
    protected void crossChromosomesSP(List<Chromosome> crossedChromosomes, Chromosome firstCrossChromosome, Chromosome secondCrossChromosome) {
        Chromosome firstChildren = firstCrossChromosome.clone();
        Chromosome secondChildren = secondCrossChromosome.clone();

        List firstChildenGenes = new LinkedList<>();
        List secondChildenGenes = new LinkedList<>();

        firstChildenGenes.addAll(firstCrossChromosome.getGenes().subList(0, nGenesPerChromosome / 2));
        firstChildenGenes.addAll(secondCrossChromosome.getGenes().subList(nGenesPerChromosome / 2, nGenesPerChromosome));

        secondChildenGenes.addAll(secondCrossChromosome.getGenes().subList(0, nGenesPerChromosome / 2));
        secondChildenGenes.addAll(firstCrossChromosome.getGenes().subList(nGenesPerChromosome / 2, nGenesPerChromosome));

        firstChildren.setGenes(firstChildenGenes);
        secondChildren.setGenes(secondChildenGenes);

        crossedChromosomes.add(firstChildren);
        crossedChromosomes.add(secondChildren);
    }

    // Uniform Crossover
    protected void crossChromosomesUC(List<Chromosome> crossedChromosomes, Chromosome firstCrossChromosome, Chromosome secondCrossChromosome) {
        Random rand = new Random();

        for (int i = 0; i < nGenesPerChromosome; ++i) {
            if (Integer.compare(firstCrossChromosome.getGenes().get(i), secondCrossChromosome.getGenes().get(i)) != 0) {
                if (rand.nextDouble() < CROSS_PROBABILITY) {
                    firstCrossChromosome.flipGene(i);
                    secondCrossChromosome.flipGene(i);
                }
            }
        }

        crossedChromosomes.add(firstCrossChromosome);
        crossedChromosomes.add(secondCrossChromosome);
    }

    // Flip bit mutation
    protected void mutatePopulation(List<Chromosome> crossedChromosomes) {
        Random rand = new Random();

        for (Chromosome crossedChromosome : crossedChromosomes) {
            for (int j = 0; j < crossedChromosome.getGenes().size(); ++j) {
                if (rand.nextDouble() < MUTATION_PROBABILITY) {
                    crossedChromosome.flipGene(j);
                }
            }
            chromosomes.add(crossedChromosome);
        }
    }

    protected Chromosome getBestChromosome(List<Chromosome> chromosomeList) {
        double aux = 0.0;
        int index = 0;

        for (int i = 0; i < chromosomeList.size(); i++) {
            if (chromosomeList.get(i).getFitnessScore() > aux) {
                aux = chromosomeList.get(i).getFitnessScore();
                index = i;
            }
        }

        return chromosomeList.get(index);
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    private void saveSolutionToClu() {
        List<String> lines = new ArrayList<>();
        lines.add("*Vertices " + nGenesPerChromosome);

        for (int i = 0; i < nGenesPerChromosome; ++i) {
            lines.add(bestChromosome.getGenes().get(i).toString());
        }

        Path file = Paths.get("solutions/" + this.saveFile + "_solution.clu");
        try {
            Files.write(file, lines, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
