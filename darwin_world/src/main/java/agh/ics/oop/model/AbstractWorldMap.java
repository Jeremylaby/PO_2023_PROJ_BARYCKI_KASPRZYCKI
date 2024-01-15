package agh.ics.oop.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();
    protected Configuration config;

    public AbstractWorldMap(Configuration configuration) {
        config = configuration;
    }

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
    public List<WorldElement> getElements() {
        return Stream.concat(
                    plants.values().stream(),
                    animals.values().stream().flatMap(Collection::stream)
                ).toList();
    }

    public Map<Vector2d, Plant> getPlants() {
        return Map.copyOf(plants);
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
                        Animal child = father.makeChild(mother, config.animalsEnergyReproduceCost());
                        place(child);
                        newborns.add(child);
                    }
                });
            });
        }

        return newborns;
    }

    private boolean canAnimalReproduce(Animal animal) {
        return animal.getEnergy() >= config.animalsEnergyToReproduce();
    }

    @Override
    public int getWidth() {
        return config.mapWidth();
    }

    @Override
    public int getHeight() {
        return config.mapHeight();
    }

    public Map<Vector2d, Set<Animal>> getAnimals() {
        return animals;
    }
}
