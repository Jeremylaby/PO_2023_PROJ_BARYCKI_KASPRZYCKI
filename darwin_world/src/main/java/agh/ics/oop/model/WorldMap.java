package agh.ics.oop.model;

import java.util.*;

public interface WorldMap {
    void place(Animal animal);

    void remove(Animal animal);

    void move(Animal animal);

    Optional<Set<Animal>> animalsAt(Vector2d position);

    List<WorldElement> getElements();

    int getWidth();

    int getHeight();

    void feedAnimals();

    boolean isAnimal(Vector2d position);

    List<Animal> reproduceAnimals();

    Map<Vector2d, Set<Animal>> getAnimals();
}