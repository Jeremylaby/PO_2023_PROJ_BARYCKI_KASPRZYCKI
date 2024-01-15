package agh.ics.oop.simulation;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Simulation implements Runnable {
    public static final int SIMULATION_INTERVAL = 1000;
    private final List<MapChangeListener> listeners = new ArrayList<>();
    private final Set<Animal> animals;
    private final WorldMap worldMap;
    private boolean paused = false;
    private boolean stopped = false;
    private int dayOfSimulation = 0;

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
        worldMap = new EquatorMap(configuration);
        animals = new HashSet<>(configuration.animalsStartNum());

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
                dayOfSimulation++;

                removeDeadAnimals();
                moveAnimals();
                feedAnimals();
                reproduceAnimals();
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

    private void removeDeadAnimals() {
        List<Animal> deadAnimals = animals.stream()
                .filter(animal -> animal.getEnergy() <= 0)
                .toList();

        deadAnimals.forEach(animal -> {
            animal.die(dayOfSimulation);
            worldMap.removeAnimal(animal);
            animals.remove(animal);
        });;
    }

    private void moveAnimals() {
        animals.forEach(worldMap::move);
    }

    private void reproduceAnimals() {
        animals.addAll(worldMap.reproduceAnimals());
    }

    private void feedAnimals() {
        worldMap.feedAnimals();
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
