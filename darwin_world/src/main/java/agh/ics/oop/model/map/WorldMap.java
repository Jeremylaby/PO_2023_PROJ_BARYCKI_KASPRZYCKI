package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.PreferredPosition;
import agh.ics.oop.model.elements.WorldElement;

import java.util.*;
import java.util.stream.Stream;

public interface WorldMap {
    void place(Animal animal);

    void remove(Animal animal);

    void move(Animal animal);

    Optional<Set<Animal>> animalsAt(Vector2d position);

    Stream<WorldElement> getElements();

    default Stream<PreferredPosition> getPreferredPlantPositions() {
        return Stream.empty();
    };

    int getWidth();

    int getHeight();

    void feedAnimals();

    boolean isAnimal(Vector2d position);

    List<Animal> reproduceAnimals();

    void growPlants();

    Map<Vector2d, Set<Animal>> getAnimals();

    Map<Vector2d, Plant> getPlants();

    int getSumOfSurvivedDays();

    int getSumOfDeadAnimals();

    void removeDeadAnimal(Animal animal, int dayOfSimulation);
}