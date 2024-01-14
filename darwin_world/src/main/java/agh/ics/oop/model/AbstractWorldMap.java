package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractWorldMap implements WorldMap {
    private final UUID id = UUID.randomUUID();
    protected Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();
    private List<MapChangeListener> observers = new ArrayList<>();
    protected Configuration conf;

    @Override
    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    protected void removePlant(Vector2d position) {
        plants.remove(position);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected Optional<Animal> findStrongest(Set<Animal> animals) {
        return animals.stream().max(Comparator.comparingInt(Animal::getEnergy).thenComparing(Animal::getAge).thenComparing(Animal::getKidsNumber));
    }

    protected void mapChanged(String message) {
        observers.forEach(observer -> observer.mapChanged(this, message));
    }

    public void place(Animal animal) {
        if (!isanimal(animal.getPosition())) {
            animals.put(animal.getPosition(), new HashSet<>());
        }
        animals.get(animal.getPosition()).add(animal);
    }

    public void move(Animal animal) {
        animals.get(animal.getPosition()).remove(animal);
        if (animals.get(animal.getPosition()).isEmpty()) {
            animals.remove(animal.getPosition());
        }
        animal.move(conf.mapWidth(), conf.mapHeight());
        place(animal);
        animals.get(animal.getPosition()).add(animal);
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

    private void feedAnimal(Animal animal) {//będę nadpisywał w poisoned land
        animal.eat(conf.plantsEnergyValue());
    }

    public boolean isanimal(Vector2d position) {
        return animals.containsKey(position);
    }

    public Set<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }


    @Override
    public Map<Vector2d, WorldElement> getElements() {
        Stream<Plant> stream = plants.values().stream().filter(plant -> !isanimal(plant.getPosition()));
        return Stream.concat(stream, animals.values().iterator().next().stream())
                .collect(Collectors.toMap(WorldElement::getPosition, worldElement -> worldElement));
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
                        && father.get().getEnergy() >= conf.animalsEnergyToReproduce()) {
                    animalsAt(key).remove(father.get());
                    Optional<Animal> mother = findStrongest(animalsAt(key));
                    animalsAt(key).add(father.get());
                    if (mother.isPresent() &&
                            mother.get().getEnergy() >= conf.animalsEnergyToReproduce()) {
                        Animal child = father.get().makeChild(mother.get(), conf.animalsEnergyReproduceCost());
                        animalsAt(key).add(child);
                    }//nie wiem czy tego nie można jakoś lepiej napisać
                }
            }
        }
    }

    @Override
    public int getWidth() {
        return 10;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void removeDeadAnimals() {
        for (Vector2d key : animals.keySet()) {
            for (Animal animal : animalsAt(key)) {
                if (animal.getEnergy() <= 0) {
                    animalsAt(key).remove(animal);
                    if (animalsAt(key).isEmpty()) {
                        animals.remove(key);
                    }
                }
            }
        }
    }
}
