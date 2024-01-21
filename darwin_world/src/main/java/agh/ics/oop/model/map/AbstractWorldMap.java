package agh.ics.oop.model.map;

import agh.ics.oop.model.Configuration;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Set<Animal>> animals;
    protected final Map<Vector2d, Plant> plants;
    protected final Configuration config;
    protected int sumOfSurvivedDays=0;
    protected int sumOfDeadAnimals=0;

    public AbstractWorldMap(Configuration configuration) {
        config = configuration;
        animals = new ConcurrentHashMap<>(config.mapWidth()*config.mapHeight());
        plants = new ConcurrentHashMap<>(config.mapWidth()*config.mapHeight());
    }

    protected abstract void generatePlants(int numOfPlants);

    protected void removePlant(Vector2d position) {
        plants.remove(position);
    }

    private Optional<Animal> findStrongest(Set<Animal> animals) {
        return animals.stream()
                .max(Comparator
                        .comparingInt(Animal::getEnergy)
                        .thenComparing(Animal::getAge)
                        .thenComparing(Animal::getKidsNumber));
    }

    public void place(Animal animal) {
        if (!isAnimal(animal.getPosition())) {
            animals.put(animal.getPosition(), new HashSet<>());
        }
        animals.get(animal.getPosition()).add(animal);
    }

    @Override
    public void move(Animal animal) {
        animalsAt(animal.getPosition())
                .ifPresent(animalsAtPosition -> {
                    if (animalsAtPosition.contains(animal)) {
                        remove(animal);
                        animal.move(config.mapWidth(), config.mapHeight());
                        place(animal);
                    }
                });
    }

    @Override
    public void remove(Animal animal) {
        animalsAt(animal.getPosition())
                .ifPresent(animalsAtPosition -> {
                    animalsAtPosition.remove(animal);
                    if (animalsAtPosition.isEmpty()) {
                        animals.remove(animal.getPosition());
                    }
                });
    }

    public void feedAnimals() {
        animals.forEach((position, animalsAtPosition) -> {
            if (plants.containsKey(position)) {
                findStrongest(animalsAtPosition).ifPresent(this::feedAnimal);
                removePlant(position);
            }
        });
    }

    protected void feedAnimal(Animal animal) {//będę nadpisywał w poisoned land
        animal.eat(config.plantsEnergyValue());
    }

    public boolean isAnimal(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public Optional<Set<Animal>> animalsAt(Vector2d position) {
        return Optional.ofNullable(animals.get(position));
    }

    @Override
    public List<Animal> reproduceAnimals() {
        List<Animal> newborns = new ArrayList<>();

        for (Set<Animal> animalSet: animals.values()) {
            findStrongest(animalSet).ifPresent(father -> {
                Set<Animal> restOfAnimals = animalSet.stream()
                        .filter(animal -> animal != father)
                        .collect(Collectors.toSet());

                findStrongest(restOfAnimals).ifPresent(mother -> {
                    if (canAnimalReproduce(father) && canAnimalReproduce(mother)) {
                        Animal child = father.makeChild(mother, config.animalsEnergyReproduceCost(), config.animalsEnergyToReproduce());
                        place(child);
                        newborns.add(child);
                    }
                });
            });
        }

        return newborns;
    }

    @Override
    public void growPlants() {
        generatePlants(config.plantsNumPerDay());
    }

    public void removeDeadAnimal(Animal animal, int dayOfSimulation){
        animal.die(dayOfSimulation);
        sumOfSurvivedDays += animal.getAge();
        sumOfDeadAnimals += 1;
        remove(animal);
    }

    private boolean canAnimalReproduce(Animal animal) {
        return animal.getEnergy() >= config.animalsEnergyToReproduce();
    }

    @Override
    public Stream<WorldElement> getElements() {
        return Stream.concat(
                plants.values().stream().filter(plant -> !animals.containsKey(plant.getPosition())),
                animals.values().stream().map(this::findStrongest).map(Optional::get)
        );
    }

    public Map<Vector2d, Plant> getPlants() {
        return Collections.unmodifiableMap(plants);
    }

    public Map<Vector2d, Set<Animal>> getAnimals() {
        return Collections.unmodifiableMap(animals);
    }

    @Override
    public int getWidth() {
        return config.mapWidth();
    }

    @Override
    public int getHeight() {
        return config.mapHeight();
    }

    public int getSumOfSurvivedDays() {
        return sumOfSurvivedDays;
    }

    public int getSumOfDeadAnimals() {
        return sumOfDeadAnimals;
    }
}
