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

    public List<Integer> getMostPopularGenes(){
        return mostPopularGenes.get(0).list();
    }

    public List<ListQuantity> getMostPopularGenesList() {
        return Collections.unmodifiableList(mostPopularGenes);
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

    public double getAvgEnergy() {
        return avgEnergy;
    }

    public double getAvgDaySurvived() {
        return avgDaySurvived;
    }

    public int getAvgKidsNumber() {
        return avgKidsNumber;
    }
}
