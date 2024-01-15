package agh.ics.oop.model;

import java.util.*;

public interface WorldMap {
    void place(Animal animal);

    void removeAnimal(Animal animal);

    void move(Animal animal);

    Collection<Animal> animalsAt(Vector2d position);

    List<WorldElement> getElements();

    int getWidth();

    int getHeight();

    void feedAnimals();

    boolean isanimal(Vector2d position);

    void reproduceAnimals();

    Map<Vector2d, Set<Animal>> getAnimals();
}