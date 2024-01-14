package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionGenerator;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final WorldMap worldMap;
    private boolean paused = false;
    private boolean stopped = false;

    public Simulation(Configuration configuration) {
//        worldMap = switch (configuration.customPlants()) {
//            case true: new PoisonousMap();
//            case false: new EquatorMap(configuration);
//        };
        worldMap = new GrassField(10);
        animals = new ArrayList<>(configuration.animalsStartNum());

        generateAnimals(configuration);
    }

    private void generateAnimals(Configuration c) {
        RandomPositionGenerator rpg = new RandomPositionGenerator(c.mapWidth(), c.mapHeight(), c.animalsStartNum());
        for (Vector2d position : rpg) {
            Genome genome = new Genome(c.customNextGene(), c.genomeMin(), c.genomeMax(), c.genomeLength());
            Animal animal = new Animal(position, genome, c.animalsStartEnergy());
            animals.add(animal);
            worldMap.place(animal);
        }
    }

    public void run() {
        while (true) {
            if (stopped) return;

            if (!paused) {
//                todo
//                animals.forEach(worldMap::cleanIfDead);
                animals.forEach(worldMap::move);
//                animals.forEach(worldMap::rotate);
//                animals.forEach(worldMap::feedAnimal);
//                worldMap.procreateAllAnimals();
//                worldMap.growPlants();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        stopped = true;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
}
