package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap {

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

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
//    default boolean isOccupied(Vector2d position) {
//        return objectAt(position) != null;
//    }

    Collection<Animal> animalsAt(Vector2d position);


    Map<Vector2d, WorldElement> getElements();
    void feedAnimals();
    boolean isanimal(Vector2d position);
    void reproduceAnimals();
    void removeDeadAnimals();
}