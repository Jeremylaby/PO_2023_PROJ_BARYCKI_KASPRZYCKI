package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.map.WorldMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {
    private final int numOfAnimals;
    private final int numOfPlants;
    private final int numOfEmptyPos;
    private final List<ListQuantity> mostPopularGenes;
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

    private List<ListQuantity> generatePopularGenes(WorldMap worldMap) {
        Map<List<Integer>,Integer> numGenes = new HashMap<>();
        List<ListQuantity> listQuantities=new ArrayList<>();
        getAnimalStream(worldMap)
                .map(animal -> animal.getGenome().getGenes()).toList()
                .forEach(gene->{numGenes.put(gene, numGenes.getOrDefault(gene,0)+1);});
        numGenes.forEach((key,value)->listQuantities.add(new ListQuantity(key,value)));
        return listQuantities.stream().sorted(Comparator.comparingInt(ListQuantity::quantity).reversed()).limit(5).toList();
    }

    private static Stream<Animal> getAnimalStream(WorldMap worldMap) {
        return worldMap.getAnimals().values().stream()
                .flatMap(Set::stream).filter(animal -> animal.getEnergy()>0);
    }

    private int calculateEmptyPos(WorldMap worldMap) {
        return worldMap.getWidth()*worldMap.getHeight()-
                Stream.concat(worldMap.getAnimals().keySet().stream(),worldMap.getPlants().keySet().stream()).distinct().toList().size();
    }
    private String formatMostPopularGenes(){
        StringBuilder genesBuilder = new StringBuilder();
        for (ListQuantity gene : mostPopularGenes) {
            genesBuilder.append(gene.list()).append(":").append(gene.quantity()).append(";");
        }
        if (genesBuilder.length() > 0) {
            genesBuilder.deleteCharAt(genesBuilder.length() - 1);
        }

        return genesBuilder.toString();
    }
    public String toCSV(){
        return String.format("%d;%d;%d;%d;%f;%f;%s",
                this.numOfAnimals,
                this.numOfPlants,
                this.numOfEmptyPos,
                this.avgKidsNumber,
                this.avgEnergy,
                this.avgDaySurvived,
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
                "\tnumOfAnimals=" + numOfAnimals +'\n'+
                "\tnumOfPlants=" + numOfPlants +'\n'+
                "\tnumOfEmptyPos=" + numOfEmptyPos +'\n'+
                "\tmostPopularGenes=\n" + mostPopularGenes +'\n'+
                "\tavgEnergy=" + avgEnergy +'\n'+
                "\tavgDaySurvived=" + avgDaySurvived +'\n'+
                "\tavgKidsNumber=" + avgKidsNumber+'\n';
    }
}
