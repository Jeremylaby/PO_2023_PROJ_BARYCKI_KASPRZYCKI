package agh.ics.oop.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StatisticsCsvWriter {
    private final String filePath;

    public StatisticsCsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public void saveToFile(int day, Statistics stats) {
        if (filePath == null) return;

        String filename = filePath + ".csv";

        boolean isNewFile = !Files.exists(Paths.get(filename));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            if (isNewFile) {
                String firstLine = "numOfDay;" +
                        "numOfAnimals;" +
                        "numOfPlants;" +
                        "numOfEmptyPos;" +
                        "avgKidsNumber;" +
                        "avgEnergy;" +
                        "avgDaySurvived;" +
                        "mostPopularGenes Top " + Statistics.TOP_GENES_SHOW_NUMBER;
                writer.write(firstLine);
                writer.newLine();
            }
            String line = toCSV(day, stats);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toCSV(int day, Statistics stats){
        return String.format("%d;%d;%d;%d;%d;%f;%f;%s",
                day,
                stats.getNumOfAnimals(),
                stats.getNumOfPlants(),
                stats.getNumOfEmptyPos(),
                stats.getAvgKidsNumber(),
                stats.getAvgEnergy(),
                stats.getAvgDaySurvived(),
                formatMostPopularGenes(stats.getMostPopularGenesList())
        );
    }

    private String formatMostPopularGenes(List<ListQuantity> mostPopularGenes){
        StringBuilder genesBuilder = new StringBuilder();
        for (ListQuantity gene : mostPopularGenes) {
            genesBuilder
                    .append(gene.getListString())
                    .append(":")
                    .append(gene.quantity())
                    .append(";");
        }
        if (!genesBuilder.isEmpty()) {
            genesBuilder.deleteCharAt(genesBuilder.length() - 1);
        }

        return genesBuilder.toString();
    }
}
