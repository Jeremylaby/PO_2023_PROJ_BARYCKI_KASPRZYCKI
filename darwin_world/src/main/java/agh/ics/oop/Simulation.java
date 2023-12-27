package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap worldMap;

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<MoveDirection> getMoves() {
        return moves;
    }

    public Simulation(List<Vector2d> positions, List<MoveDirection> moves, WorldMap worldMap) {
        this.worldMap = worldMap;
        this.animals = new ArrayList<>();
        for (Vector2d position : positions) {
            Animal animal = new Animal(position);
            try {
                worldMap.place(animal);
                animals.add(animal);
            } catch (PositionAlreadyOccupiedException e) {
                e.printStackTrace();
            }
        }
        this.moves = moves;
    }

    public void run() {
        int ind = 0;
        for (MoveDirection move : moves) {
            worldMap.move(animals.get(ind), move);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ind += 1;
            ind %= animals.size();

        }
    }
}
