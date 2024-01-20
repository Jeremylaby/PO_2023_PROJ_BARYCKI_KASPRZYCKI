package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.WorldMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Statistics {
    public static final int TOP_GENES_SHOW_NUMBER = 5;
    private final int numOfAnimals;
    private final int numOfPlants;
    private final int numOfEmptyPos;
    private final List<ListQuantity> mostPopularGenes;
    private final double avgEnergy;
    private final double avgDaySurvived;
    private final int avgKidsNumber;

    public Statistics(WorldMap worldMap) {
        numOfAnimals = worldMap.getAnimals().size();
        numOfPlants = worldMap.getPlants().size();
        numOfEmptyPos = calculateEmptyPos(worldMap);
        mostPopularGenes = calculatePopularGenes(worldMap);
        avgEnergy = calculateAvgEnergy(worldMap);
        avgDaySurvived = calculateAvgDaySurvived(worldMap);
        avgKidsNumber = calculateAvgKidsNumber(worldMap);
    }

    private int calculateAvgKidsNumber(WorldMap worldMap) {
        return Math.toIntExact(Math.round(
                getAnimalsStream(worldMap)
                .mapToInt(Animal::getKidsNumber)
                .average()
                .orElse(0.0)
        ));
    }

    private double calculateAvgDaySurvived(WorldMap worldMap) {
        return worldMap.getSumOfDeadAnimals() == 0
                ? Double.NaN
                :(double) worldMap.getSumOfSurvivedDays() / worldMap.getSumOfDeadAnimals();
    }

    private double calculateAvgEnergy(WorldMap worldMap) {
        return getAnimalsStream(worldMap)
                .mapToDouble(Animal::getEnergy)
                .average()
                .orElse(0.0);
    }

    private List<ListQuantity> calculatePopularGenes(WorldMap worldMap) {
        return getAnimalsStream(worldMap)
                .map(Animal::getGenes)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new ListQuantity(entry.getKey(), Math.toIntExact(entry.getValue())))
                .sorted(Comparator.comparingInt(ListQuantity::quantity).reversed())
                .limit(TOP_GENES_SHOW_NUMBER)
                .toList();
    }

    private static Stream<Animal> getAnimalsStream(WorldMap worldMap) {
        return worldMap.getAnimals().values().stream()
                .flatMap(Set::stream)
                .filter(animal -> animal.getEnergy() > 0);
    }

    private int calculateEmptyPos(WorldMap worldMap) {
        int numOfAllPositions = worldMap.getWidth() * worldMap.getHeight();

        int numOfOccupiedPositions = (int) Stream.concat(
                worldMap.getAnimals().keySet().stream(),
                worldMap.getPlants().keySet().stream()
        ).distinct().count();

        return numOfAllPositions - numOfOccupiedPositions;
    }

    private String formatMostPopularGenes(){
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

    public void saveToFile(UUID id, int day) {
        String filename = id + ".csv";
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
                        "mostPopularGenes Top 5";
                writer.write(firstLine);
                writer.newLine();
            }
            String line = toCSV(day);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toCSV(int day){
        return String.format("%d;%d;%d;%d;%d;%f;%f;%s",
                day,
                numOfAnimals,
                numOfPlants,
                numOfEmptyPos,
                avgKidsNumber,
                avgEnergy,
                avgDaySurvived,
                formatMostPopularGenes()
        );
    }

    public int getNumOfAnimals() {
        return numOfAnimals;
    }

    public int getNumOfPlants() {
        return numOfPlants;
    }

    public int getNumOfEmptyPos() {
        return numOfEmptyPos;
    }

    public List<ListQuantity> getMostPopularGenes() {
        return mostPopularGenes;
    }

    public double getAvgEnergy() {
        return avgEnergy;
    }

    public double getAvgDaySurvived() {
        return avgDaySurvived;
    }

    public int getAvgKidsNumber() {
        return avgKidsNumber;
    }

    @Override
    public String toString() {
        return "Statistics:" +'\n'+
                "\tnumber of animals: " + numOfAnimals +'\n'+
                "\tnumber of plants: " + numOfPlants +'\n'+
                "\tnumber of empty positions: " + numOfEmptyPos +'\n'+
                "\taverage energy: %.2f".formatted(avgEnergy) +'\n'+
                "\taverage day survived: %.2f".formatted(avgDaySurvived) +'\n'+
                "\taverage kids number: " + avgKidsNumber+'\n' +
                "\tmost popular genomes: \n" + getMostPopularGenesForDisplay();
    }

    private String getMostPopularGenesForDisplay() {
        return IntStream.range(0, mostPopularGenes.size())
                .mapToObj(i -> "%d:  %s".formatted(i+1, mostPopularGenes.get(i)))
                .collect(Collectors.joining("\t\t", "\t\t", ""));
    }
}
