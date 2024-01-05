package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final WorldMap worldMap;

    public List<Animal> getAnimals() {
        return animals;
    }

    public Simulation(List<Vector2d> positions, WorldMap worldMap) {
        this.worldMap = worldMap;
        this.animals = new ArrayList<>();
        for (Vector2d position : positions) {
            Genome initalGenome = new Genome(false, 0, 0, 10);
            Animal animal = new Animal(position, initalGenome, 100);
            worldMap.place(animal);
            animals.add(animal);

        }
    }

    public void run() {
        int ind = 0;

        while (true) {
            worldMap.move(animals.get(ind));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ind += 1;
            ind %= animals.size();

        }
    }
}
