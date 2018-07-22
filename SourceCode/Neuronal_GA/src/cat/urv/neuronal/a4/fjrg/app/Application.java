package cat.urv.neuronal.a4.fjrg.app;

import cat.urv.neuronal.a4.fjrg.ga.GeneticAlgo;
import cat.urv.neuronal.a4.fjrg.network.Network;
import cat.urv.neuronal.a4.fjrg.network.ga.NetworkGA;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        List<String> filesNames = getFiles();

        for (String fileName : filesNames) {
            System.out.println("Processing file: " + fileName);

            Network net = new Network();
            readFile(net, "data/" + fileName + ".net");

            GeneticAlgo GA = new NetworkGA(100, net);
            GA.setSaveFile(fileName);

            Long initialTime = System.currentTimeMillis();
            GA.run();
            System.out.println("Execution time: " + (String.valueOf((System.currentTimeMillis() - initialTime) / 1000.0)) + "s.\n");
        }

    }

    private static void readFile(Network net, String pathToFile) {
        boolean readingVertices = true;

        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(pathToFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                String[] lineSplitted = strLine.trim().split(" ");

                if (strLine.contains("Edges")) {
                    readingVertices = false;
                }

                if (readingVertices && !strLine.contains("Vertices")) {
                    net.addVertex(Integer.parseInt(lineSplitted[0]));
                }

                if (!readingVertices && !strLine.contains("Edges")) {
                    net.addEdge(Integer.parseInt(lineSplitted[0]), Integer.parseInt(lineSplitted[1]));
                }

            }

            //Close the input stream
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        net.finishConstruction();
    }

    private static List<String> getFiles() {
        List<String> filesNames = new LinkedList<>();

//        filesNames.add("graph3+1+3");
//        filesNames.add("star");
//        filesNames.add("circle9");
//        filesNames.add("graph3+2+3");
//        filesNames.add("wheel");
//        filesNames.add("clique_stars");
//        filesNames.add("rb25");
//        filesNames.add("grid-6x6");
//        filesNames.add("grid-p-6x6");
//        filesNames.add("zachary_unwh");
//        filesNames.add("dolphins");
//        filesNames.add("cliques_line");
//        filesNames.add("qns04_d");
//        filesNames.add("rhesus_simetrica");
//        filesNames.add("20x2+5x2");
//        filesNames.add("adjnoun");
//        filesNames.add("rb125");
//        filesNames.add("cat_cortex_sim");
        filesNames.add("256_4_4_4_13_18_p");
//        filesNames.add("256_4_4_2_15_18_p");

        return filesNames;
    }

}
