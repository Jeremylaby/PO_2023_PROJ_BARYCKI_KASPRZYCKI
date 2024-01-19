package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.map.WorldMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {
    private final int numOfAnimals;
    private final int numOfPlants;
    private final int numOfEmptyPos;
    private final Map<List<Integer>,Integer> mostPopularGenes;
    private final double avgEnergy;
    private final double avgDaySurvived;
    private final int avgKidsNumber;

    public Statistics(WorldMap worldMap) {
        this.numOfAnimals = worldMap.getAnimals().size();
        this.numOfPlants = worldMap.getPlants().size();
        this.numOfEmptyPos = calculateEmptyPos(worldMap);
        this.mostPopularGenes = generatePopularGenes(worldMap);
        this.avgEnergy = calculateavgEnergy(worldMap);
        this.avgDaySurvived = calculateavgDaySurvived(worldMap);
        this.avgKidsNumber = calculateavgKidsNumber(worldMap);
    }

    private int calculateavgKidsNumber(WorldMap worldMap) {
        return Math.toIntExact(Math.round(getAnimalStream(worldMap).mapToDouble(Animal::getKidsNumber).average().orElse(0.0)));
    }

    private double calculateavgDaySurvived(WorldMap worldMap) {
        return worldMap.getSumOfDeadAnimals()==0
                ? Double.NaN
                :(double) worldMap.getSumOfSurvivedDays() /worldMap.getSumOfDeadAnimals();
    }

    private double calculateavgEnergy(WorldMap worldMap) {
        return getAnimalStream(worldMap).mapToDouble(Animal::getEnergy).average()
                .orElse(0.0);
    }

    private Map<List<Integer>, Integer> generatePopularGenes(WorldMap worldMap) {
        Map<List<Integer>,Integer> numGenes = new HashMap<>();
        getAnimalStream(worldMap)
                .map(animal -> animal.getGenome().getGenes()).toList()
                .forEach(gene->{numGenes.put(gene, numGenes.getOrDefault(gene,0)+1);});
        return numGenes;
    }

    private static Stream<Animal> getAnimalStream(WorldMap worldMap) {
        return worldMap.getAnimals().values().stream()
                .flatMap(Set::stream);
    }

    private int calculateEmptyPos(WorldMap worldMap) {
        return worldMap.getWidth()*worldMap.getHeight()-
                Stream.concat(worldMap.getAnimals().keySet().stream(),worldMap.getPlants().keySet().stream()).distinct().toList().size();
    }
}
