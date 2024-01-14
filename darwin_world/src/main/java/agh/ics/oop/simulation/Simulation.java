package agh.ics.oop.simulation;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    public static final int SIMULATION_INTERVAL = 500;
    private final List<MapChangeListener> listeners = new ArrayList<>();
    private final List<Animal> animals;
    private final WorldMap worldMap;
    private boolean paused = false;
    private boolean stopped = false;

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    public Simulation(Configuration configuration) {
//        worldMap = switch (configuration.customPlants()) {
//            case true: new PoisonousMap(configuration);
//            case false: new EquatorMap(configuration);
//        };
        worldMap = new GrassField(configuration);
        animals = new ArrayList<>(configuration.animalsStartNum());

        generateAnimals(configuration);
    }

    private void generateAnimals(Configuration c) {
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(c.mapWidth(), c.mapHeight(), c.animalsStartNum());
        for (Vector2d position : positionGenerator) {
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
                animals.forEach(animal -> System.out.println(animal.getEnergy()));
//                animals.forEach(worldMap::feedAnimal);
//                worldMap.procreateAllAnimals();
//                worldMap.growPlants();
                mapChanged();
            }

            try {
                Thread.sleep(SIMULATION_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void mapChanged() {
        listeners.forEach(listener -> listener.mapChanged(worldMap, ""));
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
