package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap {

    void addObserver(MapChangeListener observer);

    void removeObserver(MapChangeListener observer);

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal);

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    Collection<Animal> animalsAt(Vector2d position);

    Map<Vector2d, WorldElement> getElements();

    int getWidth();

    int getHeight();
}