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

    protected Optional<Animal> findStrongest(Set<Animal> animals) {
        return animals.stream().max(Comparator.comparingInt(Animal::getEnergy).thenComparing(Animal::getAge).thenComparing(Animal::getKidsNumber));
    }

    public void place(Animal animal) {
        if (!isanimal(animal.getPosition())) {
            animals.put(animal.getPosition(), new HashSet<>());
        }
        animals.get(animal.getPosition()).add(animal);
    }

    @Override
    public void move(Animal animal) {
        if (animalsAt(animal.getPosition()) != null && animalsAt(animal.getPosition()).contains(animal)) {
            removeAnimal(animal);
            animal.move(config.mapWidth(), config.mapHeight());
            place(animal);
        }
    }

    @Override
    public void removeAnimal(Animal animal) {
        if (animals.get(animal.getPosition()) == null) {
            return;
        }
        animals.get(animal.getPosition()).remove(animal);
        if (animals.get(animal.getPosition()).isEmpty()) {
            animals.remove(animal.getPosition());
        }
    }

    public void feedAnimals() {
        for (Vector2d key : animals.keySet()) {
            if (plants.containsKey(key)) {
                if (animalsAt(key).size() == 1) {
                    feedAnimal(animalsAt(key).iterator().next());
                } else {
                    findStrongest(animalsAt(key))
                            .ifPresent(this::feedAnimal);

                }
                removePlant(key);
            }
        }
    }

    protected void feedAnimal(Animal animal) {//będę nadpisywał w poisoned land
        animal.eat(config.plantsEnergyValue());
    }

    public boolean isanimal(Vector2d position) {
        return animals.containsKey(position);
    }

    public Set<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        Stream<Plant> stream = plants.values().stream().filter(plant -> !isanimal(plant.getPosition()));
        return Stream.concat(stream, animals.values().stream().flatMap(Collection::stream))
                .toList();
    }

    public Map<Vector2d, Plant> getPlants() {
        return Map.copyOf(plants);
    }

    @Override
    public void reproduceAnimals() {
        for (Vector2d key : animals.keySet()) {
            if (animalsAt(key).size() >= 2) {
                Optional<Animal> father = findStrongest(animalsAt(key));
                if (father.isPresent()
                        && father.get().getEnergy() >= config.animalsEnergyToReproduce()) {
                    animalsAt(key).remove(father.get());
                    Optional<Animal> mother = findStrongest(animalsAt(key));
                    animalsAt(key).add(father.get());
                    if (mother.isPresent() &&
                            mother.get().getEnergy() >= config.animalsEnergyToReproduce()) {
                        Animal child = father.get().makeChild(mother.get(), config.animalsEnergyReproduceCost());
                        animalsAt(key).add(child);
                    }//nie wiem czy tego nie można jakoś lepiej napisać
                }
            }
        }
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
